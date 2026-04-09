const DEFAULT_USD_TO_INR_RATE = 83;

const usdToInrRate = Number(import.meta.env.VITE_USD_TO_INR_RATE ?? DEFAULT_USD_TO_INR_RATE);

const inrFormatter = new Intl.NumberFormat("en-IN", {
  style: "currency",
  currency: "INR",
  maximumFractionDigits: 2
});

export function formatInr(value: number) {
  return inrFormatter.format(value * usdToInrRate);
}
