package com.shopnest.config;

import com.shopnest.model.Product;
import com.shopnest.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Adds sample products the first time the app runs (only if the table is empty).
 * Images come from loremflickr.com (picked by keyword). Replace any imageUrl with your own link.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private String img(String keyword, int lock) {
        return "https://loremflickr.com/500/400/" + keyword + "?lock=" + lock;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) return;

        productRepository.save(new Product("Sketch Book A4", "120 GSM thick paper, 100 pages. Perfect for pencil, charcoal and ink drawings.", 249, 399, "Stationery", 60, "https://m.media-amazon.com/images/I/71UiybHeNpL._AC_UF894,1000_QL80_.jpg"));
        productRepository.save(new Product("Gel Pen Set (20 Colours)", "Smooth-writing gel pens in 20 vibrant colours. Great for notes and doodling.", 149, 199, "Stationery", 120, "https://m.media-amazon.com/images/I/81gkenYyLIL.jpg"));
        productRepository.save(new Product("Spiral Notebook (Pack of 4)", "Ruled spiral notebooks, 200 pages each, with a strong cover.", 199, 299, "Stationery", 80, "https://www.mystore.in/s/62ea2c599d1398fa16dbae0a/g/69a2fff504485265e191dfe7/1-1024x1024.jpg"));
        productRepository.save(new Product("Watercolour Paint Set", "24 colour watercolour palette with a brush. Ideal for beginners and hobby artists.", 349, 499, "Stationery", 45, "https://www.nicpro.com/cdn/shop/files/MG136-Z5.jpg?v=1724145165&width=1600"));

        productRepository.save(new Product("Wireless Earbuds", "Bluetooth 5.3 earbuds with deep bass, touch controls and 24 hours of battery with the case.", 1499, 2999, "Electronics", 40, "https://m.media-amazon.com/images/I/61pPU9PmrtL._AC_UF1000,1000_QL80_.jpg"));
        productRepository.save(new Product("Bluetooth Speaker", "Portable waterproof speaker with 12 hours of playtime and punchy sound.", 1299, 2199, "Electronics", 35, "https://media-ik.croma.com/Croma%20Assets/Entertainment/Speakers%20and%20Media%20Players/Images/251614_0_ghxuff.png"));
        productRepository.save(new Product("Power Bank 10000mAh", "Slim fast-charging power bank with dual USB output and LED indicator.", 999, 1599, "Electronics", 70, "https://www.ugreenindia.com/cdn/shop/files/257421.webp?v=1763835048&width=1214"));
        productRepository.save(new Product("Smart Watch", "1.8 inch display, heart-rate and sleep tracking, 7-day battery, IP67 water resistance.", 2499, 4999, "Electronics", 25, "https://m.media-amazon.com/images/I/71XA0QCW5lL.jpg"));

        productRepository.save(new Product("Cotton T-Shirt", "100% soft cotton round-neck t-shirt. Comfortable for everyday wear.", 399, 799, "Fashion", 100, "https://kiton.com/cdn/shop/files/UMK088401_1.jpg?v=1772530724"));
        productRepository.save(new Product("Denim Jacket", "Classic blue denim jacket with a regular fit. Goes with everything.", 1799, 2999, "Fashion", 30, "https://xcdn.next.co.uk/common/items/default/default/itemimages/3_4Ratio/product/lge/W63531s7.jpg?im=Resize,width=750"));
        productRepository.save(new Product("Running Shoes", "Lightweight breathable shoes with a cushioned sole for running and gym.", 1999, 3499, "Fashion", 50, "https://img.tatacliq.com/images/i27//437Wx649H/MP000000011087361_437Wx649H_202510122252051.jpeg"));
        productRepository.save(new Product("College Backpack", "30 litre backpack with laptop sleeve, water-resistant fabric and padded straps.", 899, 1499, "Fashion", 65, "https://annachy-prod-assets.annachy.com/pims/products-v1/BGBP00011BRO_2-image1_v1780483786091.webp"));

        productRepository.save(new Product("Ceramic Coffee Mug Set (2)", "Set of two 300 ml ceramic mugs, microwave and dishwasher safe.", 349, 599, "Home & Kitchen", 75, "https://femora.in/cdn/shop/files/FMBNNSHBLKMRL_1.jpg?v=1719041481"));
        productRepository.save(new Product("Study Table Lamp", "LED desk lamp with 3 brightness levels and a flexible neck.", 699, 1199, "Home & Kitchen", 55, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRtVmKZVNNxJ4OS627bx-WDuIzDI4usvNnAjUvF96rsig&s=10"));
        productRepository.save(new Product("Steel Water Bottle 1L", "Double-wall insulated stainless steel bottle. Keeps drinks cold for 24 hours.", 399, 699, "Home & Kitchen", 90, "https://rukminim2.flixcart.com/image/480/640/xif0q/shopsy-bottle/m/l/r/1000-steel-water-bottle-fridge-water-bottle-steel-bottle-for-original-imahhjf4gxhw4wzp.jpeg?q=90"));
        productRepository.save(new Product("Cotton Bedsheet (Double)", "Soft printed cotton double bedsheet with 2 pillow covers.", 799, 1299, "Home & Kitchen", 40, "https://instamart-media-assets.swiggy.com/swiggy/image/upload/fl_lossy,f_auto,q_auto,h_600/NI_CATALOG/IMAGES/ciw/2026/7/4/09cc2999-bc83-4d67-ab54-ada7e7900a3c_1_MN_4bbb1f21-cb14-4dde-ae8c-fb690a650b9b.png"));
    }
}
