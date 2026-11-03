package task2;

import java.util.HashMap;
import java.util.Map;

public class CachedStockProxy implements StockProvider {
    private static class CacheEntry {
        private final double price;
        private final long timestamp;

        private CacheEntry(double price, long timestamp) {
            this.price = price;
            this.timestamp = timestamp;
        }
    }

    private final StockProvider realProvider;
    private final long ttlMillis;
    private final Map<String, CacheEntry> priceCache = new HashMap<String, CacheEntry>();

    public CachedStockProxy(StockProvider realProvider, long ttlMillis) {
        if (realProvider == null) {
            throw new IllegalArgumentException("Real provider cannot be null");
        }
        if (ttlMillis < 0) {
            throw new IllegalArgumentException("TTL cannot be negative");
        }
        this.realProvider = realProvider;
        this.ttlMillis = ttlMillis;
    }

    @Override
    public synchronized double getMarketPrice(String ticker) {
        if (ticker == null || ticker.trim().isEmpty()) {
            throw new IllegalArgumentException("Ticker cannot be empty");
        }

        String cacheKey = ticker.toUpperCase();
        long now = System.currentTimeMillis();
        CacheEntry entry = priceCache.get(cacheKey);

        if (entry != null && now - entry.timestamp < ttlMillis) {
            System.out.printf("[CACHE HIT] Returning cached price for %s: $%.2f (Age: %d ms)%n",
                    ticker, entry.price, now - entry.timestamp);
            return entry.price;
        }

        System.out.printf("[NETWORK FETCH] Cache miss or expired for %s. Calling upstream adapter...%n", ticker);
        double freshPrice = realProvider.getMarketPrice(ticker);
        priceCache.put(cacheKey, new CacheEntry(freshPrice, System.currentTimeMillis()));
        return freshPrice;
    }

    @Override
    public String getStatus(String ticker) {
        return realProvider.getStatus(ticker);
    }
}
