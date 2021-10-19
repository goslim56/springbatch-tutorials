package io.bigin.biginwebhookprocessor.http;

public class ShopifyWebhookHeader {
    public static String TOPIC = "X-Shopify-Topic";
    public static String HMAC = "X-Shopify-Hmac-Sha256";
    public static String SHOP_DOMAIN = "X-Shopify-Shop-Domain";
    public static String API_VERSION = "X-Shopify-API-Version";
    public static String WEBHOOK_ID = "X-Shopify-Webhook-Id";
}
