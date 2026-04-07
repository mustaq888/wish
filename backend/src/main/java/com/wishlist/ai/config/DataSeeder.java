package com.wishlist.ai.config;

import com.wishlist.ai.domain.PriceComparison;
import com.wishlist.ai.domain.Product;
import com.wishlist.ai.domain.Role;
import com.wishlist.ai.domain.User;
import com.wishlist.ai.repository.PriceComparisonRepository;
import com.wishlist.ai.repository.ProductRepository;
import com.wishlist.ai.repository.UserRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private static final BigDecimal[] PRICE_FACTORS = {
            new BigDecimal("0.96"),
            new BigDecimal("0.98"),
            new BigDecimal("1.00"),
            new BigDecimal("1.01"),
            new BigDecimal("1.03"),
            new BigDecimal("1.05")
    };

    private static final BigDecimal[] SHIPPING_COSTS = {
            new BigDecimal("0.00"),
            new BigDecimal("1.49"),
            new BigDecimal("0.00"),
            new BigDecimal("2.99"),
            new BigDecimal("3.49"),
            new BigDecimal("0.99")
    };

    private static final BigDecimal[] CONFIDENCE_SCORES = {
            new BigDecimal("0.97"),
            new BigDecimal("0.95"),
            new BigDecimal("0.93"),
            new BigDecimal("0.91"),
            new BigDecimal("0.89"),
            new BigDecimal("0.87")
    };

    private final ProductRepository productRepository;
    private final PriceComparisonRepository priceComparisonRepository;
    private final UserRepository userRepository;

    @Bean
    CommandLineRunner seedMongoData() {
        return args -> {
            // Seed users first
            Map<String, User> existingUsersByEmail = userRepository.findAll().stream()
                    .collect(Collectors.toMap(user -> normalize(user.getEmail()), Function.identity(), (left, right) -> left));

            for (var seed : userSeeds()) {
                User savedUser = existingUsersByEmail.get(normalize(seed.email()));
                if (savedUser == null) {
                    savedUser = userRepository.save(buildUser(seed));
                    existingUsersByEmail.put(normalize(seed.email()), savedUser);
                }
            }

            // Seed products
            Map<String, Product> existingByName = productRepository.findAll().stream()
                    .collect(Collectors.toMap(product -> normalize(product.getName()), Function.identity(), (left, right) -> left));

            for (var seed : productSeeds()) {
                Product savedProduct = existingByName.get(normalize(seed.name()));
                if (savedProduct == null) {
                    savedProduct = productRepository.save(buildProduct(seed));
                    existingByName.put(normalize(seed.name()), savedProduct);
                }
                seedMissingComparisons(savedProduct, seed);
            }
        };
    }

    private void seedMissingComparisons(Product product, ProductSeed seed) {
        Set<String> existingPlatforms = priceComparisonRepository.findByProductIdOrderByListedPriceAsc(product.getId()).stream()
                .map(PriceComparison::getPlatformName)
                .map(this::normalize)
                .collect(Collectors.toSet());

        comparisonSeedsFor(seed).stream()
                .filter(comparison -> !existingPlatforms.contains(normalize(comparison.platform())))
                .map(comparison -> buildComparison(
                        product.getId(),
                        comparison.platform(),
                        comparison.url(),
                        comparison.listedPrice(),
                        comparison.shippingCost(),
                        comparison.confidence()))
                .forEach(priceComparisonRepository::save);
    }

    private User buildUser(UserSeed seed) {
        var user = new User();
        user.setFullName(seed.fullName());
        user.setEmail(seed.email());
        user.setPassword(seed.password());
        user.setRole(seed.role());
        return user;
    }

    private List<UserSeed> userSeeds() {
        return List.of(
                seedUser("John Doe", "john.doe@example.com", "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi", Role.ROLE_USER), // password: password
                seedUser("Jane Smith", "jane.smith@example.com", "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi", Role.ROLE_USER), // password: password
                seedUser("Admin User", "admin@example.com", "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi", Role.ROLE_ADMIN), // password: password
                seedUser("Alice Johnson", "alice.johnson@example.com", "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi", Role.ROLE_USER), // password: password
                seedUser("Bob Wilson", "bob.wilson@example.com", "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi", Role.ROLE_USER)  // password: password
        );
    }

    private UserSeed seedUser(String fullName, String email, String password, Role role) {
        return new UserSeed(fullName, email, password, role);
    }

    private Product buildProduct(ProductSeed seed) {
        var product = new Product();
        product.setName(seed.name());
        product.setDescription(seed.description());
        product.setCategory(seed.category());
        product.setBrand(seed.brand());
        product.setImageUrl(seed.imageUrl());
        product.setBasePrice(new BigDecimal(seed.basePrice()));
        product.setAverageRating(new BigDecimal(seed.averageRating()));
        return product;
    }

    private List<ComparisonSeed> comparisonSeedsFor(ProductSeed seed) {
        var platforms = switch (seed.category()) {
            case "Computers", "Mobiles", "Audio", "Television" -> List.of("Amazon", "Flipkart", "Croma", "Reliance Digital", "Vijay Sales", "Tata Cliq");
            case "Household", "Kitchen", "Appliances" -> List.of("Amazon", "Flipkart", "JioMart", "Blinkit", "Pepperfry", "Ikea");
            case "Furniture" -> List.of("Pepperfry", "Ikea", "Amazon", "Flipkart", "JioMart", "Tata Cliq");
            case "Fashion" -> List.of("Myntra", "Ajio", "Amazon", "Flipkart", "Tata Cliq", "Nykaa Fashion");
            case "Beauty" -> List.of("Nykaa", "Amazon", "Flipkart", "Ajio", "Myntra", "Tata Cliq");
            default -> List.of("Amazon", "Flipkart", "Croma", "Reliance Digital", "JioMart", "Tata Cliq");
        };

        var basePrice = new BigDecimal(seed.basePrice());
        var slug = seed.name().toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");

        return java.util.stream.IntStream.range(0, platforms.size())
                .mapToObj(index -> {
                    var listedPrice = basePrice.multiply(PRICE_FACTORS[index]).setScale(2, RoundingMode.HALF_UP);
                    return new ComparisonSeed(
                            platforms.get(index),
                            "https://" + platforms.get(index).toLowerCase(Locale.ROOT).replace(" ", "") + ".example/product/" + slug,
                            listedPrice.toPlainString(),
                            SHIPPING_COSTS[index].toPlainString(),
                            CONFIDENCE_SCORES[index].toPlainString());
                })
                .toList();
    }

    private PriceComparison buildComparison(String productId, String platform, String url, String listedPrice, String shippingCost, String confidence) {
        var comparison = new PriceComparison();
        comparison.setProductId(productId);
        comparison.setPlatformName(platform);
        comparison.setProductUrl(url);
        comparison.setListedPrice(new BigDecimal(listedPrice));
        comparison.setShippingCost(new BigDecimal(shippingCost));
        comparison.setConfidenceScore(new BigDecimal(confidence));
        comparison.setScrapedAt(Instant.now());
        comparison.setInStock(true);
        return comparison;
    }

    private String normalize(String value) {
        return value.toLowerCase(Locale.ROOT).trim();
    }

    private List<ProductSeed> productSeeds() {
        return List.of(
                seed("Dell Inspiron 15", "Reliable everyday laptop for office work, browsing, and study.", "Computers", "Dell", "https://images.unsplash.com/photo-1496181133206-80ce9b88a853", "749.99", "4.5"),
                seed("HP Pavilion Aero", "Lightweight performance laptop with vivid display and long battery life.", "Computers", "HP", "https://images.unsplash.com/photo-1517336714739-489689fd1ca8", "829.99", "4.4"),
                seed("Lenovo IdeaPad Slim 5", "Slim productivity laptop tuned for students and creators.", "Computers", "Lenovo", "https://images.unsplash.com/photo-1518770660439-4636190af475", "689.99", "4.3"),
                seed("Apple MacBook Air M3", "Fast ultrabook with excellent battery backup and premium build.", "Computers", "Apple", "https://images.unsplash.com/photo-1515879218367-8466d910aaa4", "1199.99", "4.8"),
                seed("ASUS Vivobook 16", "Big-screen laptop with modern design and balanced performance.", "Computers", "ASUS", "https://images.unsplash.com/photo-1509395176047-4a66953fd231", "779.99", "4.4"),
                seed("iPhone 15", "Flagship Apple phone with strong cameras and smooth performance.", "Mobiles", "Apple", "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9", "799.99", "4.8"),
                seed("Samsung Galaxy S24", "Android flagship with bright AMOLED display and AI features.", "Mobiles", "Samsung", "https://images.unsplash.com/photo-1610945265064-0e34e5519bbf", "849.99", "4.7"),
                seed("OnePlus 12R", "High-refresh performance phone with fast charging and clean UI.", "Mobiles", "OnePlus", "https://images.unsplash.com/photo-1598327105666-5b89351aff97", "549.99", "4.5"),
                seed("Nothing Phone 2a", "Minimal design smartphone with a smooth UI and good cameras.", "Mobiles", "Nothing", "https://images.unsplash.com/photo-1580910051074-3eb694886505", "399.99", "4.3"),
                seed("Xiaomi Redmi Note 13 Pro", "Value-focused phone with strong camera hardware.", "Mobiles", "Xiaomi", "https://images.unsplash.com/photo-1567581935884-3349723552ca", "329.99", "4.2"),
                seed("Sony WH-1000XM5", "Premium headphones with class-leading noise cancellation.", "Audio", "Sony", "https://images.unsplash.com/photo-1505740420928-5e560c06d30e", "349.99", "4.8"),
                seed("JBL Flip 6", "Portable Bluetooth speaker with punchy sound and rugged build.", "Audio", "JBL", "https://images.unsplash.com/photo-1546435770-a3e426bf472b", "129.99", "4.4"),
                seed("Samsung Galaxy Buds Live", "Wireless earbuds with active noise cancellation and rich bass.", "Audio", "Samsung", "https://images.unsplash.com/photo-1588423771073-b8903fbb85b5", "129.99", "4.4"),
                seed("boAt Airdopes 411", "Budget earbuds for workouts, travel, and daily calls.", "Audio", "boAt", "https://images.unsplash.com/photo-1606220588913-b3aacb4d2f46", "49.99", "4.1"),
                seed("Marshall Emberton II", "Compact premium speaker with a warm signature and long playback.", "Audio", "Marshall", "https://images.unsplash.com/photo-1572569511254-d8f925fe2cbb", "179.99", "4.6"),
                seed("Sony Bravia 55 4K", "Cinematic 4K smart TV with vivid HDR colors.", "Television", "Sony", "https://images.unsplash.com/photo-1593784991095-a205069470b6", "899.99", "4.7"),
                seed("Samsung Crystal Vision 50", "Slim smart TV with bright colors and streaming apps built in.", "Television", "Samsung", "https://images.unsplash.com/photo-1461151304267-38535e780c79", "649.99", "4.5"),
                seed("LG OLED C4 55", "Premium OLED television for movies, gaming, and sports.", "Television", "LG", "https://images.unsplash.com/photo-1593359677879-a4bb92f829d1", "1299.99", "4.8"),
                seed("Xiaomi Smart TV X 43", "Affordable 4K television with Google TV experience.", "Television", "Xiaomi", "https://images.unsplash.com/photo-1593305841991-05c297ba4575", "479.99", "4.2"),
                seed("TCL QLED Pro 55", "QLED smart television with strong contrast and app support.", "Television", "TCL", "https://images.unsplash.com/photo-1571415060716-baff5f717c37", "719.99", "4.3"),
                seed("Philips LED Smart Bulb Pack", "Energy-saving smart bulbs with app and voice control.", "Household", "Philips", "https://images.unsplash.com/photo-1513506003901-1e6a229e2d15", "39.99", "4.5"),
                seed("Syska Emergency LED Bulb", "Backup lighting bulb for power cuts and night use.", "Household", "Syska", "https://images.unsplash.com/photo-1524484485831-a92ffc0de03f", "19.99", "4.1"),
                seed("Homefab Laundry Basket", "Foldable laundry hamper for compact homes and apartments.", "Household", "Homefab", "https://images.unsplash.com/photo-1484154218962-a197022b5858", "24.99", "4.0"),
                seed("Milton Vacuum Cleaning Set", "Cleaning starter bundle with storage and easy handling.", "Household", "Milton", "https://images.unsplash.com/photo-1581578731548-c64695cc6952", "34.99", "4.1"),
                seed("Scotch-Brite Floor Cleaning Kit", "Everyday floor care kit with mop and refill accessories.", "Household", "Scotch-Brite", "https://images.unsplash.com/photo-1563453392212-326f5e854473", "29.99", "4.2"),
                seed("Prestige Induction Cooktop", "Quick electric cooktop with safety lock and timer support.", "Kitchen", "Prestige", "https://images.unsplash.com/photo-1556911220-bff31c812dba", "79.99", "4.3"),
                seed("Pigeon 3 Burner Gas Stove", "Compact stovetop for daily family cooking needs.", "Kitchen", "Pigeon", "https://images.unsplash.com/photo-1504674900247-0877df9cc836", "119.99", "4.2"),
                seed("Milton Thermosteel Bottle Set", "Insulated bottle set for home, office, and travel.", "Kitchen", "Milton", "https://images.unsplash.com/photo-1547592180-85f173990554", "34.99", "4.4"),
                seed("Borosil Glass Lunch Box Kit", "Durable lunch box kit with microwave-safe containers.", "Kitchen", "Borosil", "https://images.unsplash.com/photo-1473093295043-cdd812d0e601", "27.99", "4.1"),
                seed("Wonderchef Nonstick Cookware Combo", "Kitchen combo pack for frying, sauteing, and simmering.", "Kitchen", "Wonderchef", "https://images.unsplash.com/photo-1514986888952-8cd320577b68", "89.99", "4.3"),
                seed("LG Front Load Washing Machine", "Efficient washer with steam clean and inverter motor.", "Appliances", "LG", "https://images.unsplash.com/photo-1626806787461-102c1bfaaea1", "699.99", "4.6"),
                seed("Samsung Double Door Refrigerator", "Spacious family refrigerator with digital inverter cooling.", "Appliances", "Samsung", "https://images.unsplash.com/photo-1584568694244-14fbdf83bd30", "949.99", "4.5"),
                seed("Dyson V12 Detect Slim", "Premium cordless vacuum with laser dust detection.", "Appliances", "Dyson", "https://images.unsplash.com/photo-1558317374-067fb5f30001", "599.99", "4.7"),
                seed("Crompton Desert Cooler", "Powerful air cooler for summer comfort in large rooms.", "Appliances", "Crompton", "https://images.unsplash.com/photo-1564540574859-0dfb63985997", "229.99", "4.1"),
                seed("IFB Microwave Oven 30L", "Convection microwave for baking, reheating, and grilling.", "Appliances", "IFB", "https://images.unsplash.com/photo-1574269909862-7e1d70bb8078", "249.99", "4.3"),
                seed("Wakefit Study Table", "Modern work desk designed for compact study corners.", "Furniture", "Wakefit", "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85", "189.99", "4.2"),
                seed("Ikea Poang Accent Chair", "Relaxed reading chair with curved frame and soft cushion.", "Furniture", "Ikea", "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85", "249.99", "4.4"),
                seed("Pepperfry Storage Bed", "Platform bed with hydraulic storage and upholstered headboard.", "Furniture", "Pepperfry", "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85", "549.99", "4.3"),
                seed("Nilkamal Bookshelf", "Simple bookshelf unit for files, books, and decor.", "Furniture", "Nilkamal", "https://images.unsplash.com/photo-1484101403633-562f891dc89a", "159.99", "4.1"),
                seed("Urban Ladder Sofa 3 Seater", "Comfort-first living room sofa with durable fabric.", "Furniture", "Urban Ladder", "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85", "699.99", "4.5"),
                seed("Nike Air Zoom Pulse", "Comfortable lifestyle sneakers with premium cushioning.", "Fashion", "Nike", "https://images.unsplash.com/photo-1542291026-7eec264c27ff", "119.99", "4.6"),
                seed("Adidas Essentials Hoodie", "Soft hoodie for daily wear, travel, and layering.", "Fashion", "Adidas", "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab", "69.99", "4.4"),
                seed("Levis 511 Slim Jeans", "Classic slim-fit denim for casual and semi-formal looks.", "Fashion", "Levis", "https://images.unsplash.com/photo-1542272604-787c3835535d", "59.99", "4.3"),
                seed("Puma Running Shorts", "Breathable activewear shorts for gym and jog sessions.", "Fashion", "Puma", "https://images.unsplash.com/photo-1515886657613-9f3515b0c78f", "29.99", "4.2"),
                seed("Biba Printed Kurta Set", "Festive-ready ethnic wear with soft fabric and easy fit.", "Fashion", "Biba", "https://images.unsplash.com/photo-1529139574466-a303027c1d8b", "79.99", "4.3"),
                seed("Minimalist Vitamin C Serum", "Daily brightening serum for a smooth and radiant look.", "Beauty", "Minimalist", "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9", "16.99", "4.5"),
                seed("Mamaearth Onion Hair Kit", "Hair care combo designed for stronger-looking strands.", "Beauty", "Mamaearth", "https://images.unsplash.com/photo-1515377905703-c4788e51af15", "24.99", "4.1"),
                seed("Maybelline Fit Me Foundation", "Popular liquid foundation with a natural matte finish.", "Beauty", "Maybelline", "https://images.unsplash.com/photo-1596462502278-27bfdc403348", "12.99", "4.4"),
                seed("Lakme Sun Expert SPF 50", "Everyday sunscreen for light, comfortable UV protection.", "Beauty", "Lakme", "https://images.unsplash.com/photo-1556228578-8c89e6adf883", "11.99", "4.2"),
                seed("Nykaa Matte Lip Crayon", "Bold daily lip color with smooth matte payoff.", "Beauty", "Nykaa", "https://images.unsplash.com/photo-1586495777744-4413f21062fa", "9.99", "4.3")
        );
    }

    private ProductSeed seed(String name, String description, String category, String brand, String imageUrl, String basePrice, String averageRating) {
        return new ProductSeed(name, description, category, brand, imageUrl, basePrice, averageRating);
    }

    private record ProductSeed(
            String name,
            String description,
            String category,
            String brand,
            String imageUrl,
            String basePrice,
            String averageRating) {
    }

    private record ComparisonSeed(String platform, String url, String listedPrice, String shippingCost, String confidence) {
    }

    private record UserSeed(String fullName, String email, String password, Role role) {
    }
}
