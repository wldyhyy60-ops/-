package com.example.data.local

import com.example.data.model.ActivityLogEntity
import com.example.data.model.CategoryEntity
import com.example.data.model.CouponEntity
import com.example.data.model.DeliveryZoneEntity
import com.example.data.model.DiscountType
import com.example.data.model.NotificationEntity
import com.example.data.model.OrderEntity
import com.example.data.model.OrderItemEntity
import com.example.data.model.OrderStatus
import com.example.data.model.PaymentMethodEntity
import com.example.data.model.PaymentStatus
import com.example.data.model.PaymentType
import com.example.data.model.ProductEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import kotlinx.coroutines.flow.first

object DatabaseInitializer {

    suspend fun populateInitialDataIfEmpty(db: AppDatabase) {
        val existingCategories = db.categoryDao().getAllCategories().first()
        if (existingCategories.isNotEmpty()) return

        // 1. Categories
        val categories = listOf(
            CategoryEntity(
                id = 1,
                nameAr = "الإلكترونيات",
                nameEn = "Electronics",
                iconKey = "bolt",
                description = "أجهزة إلكترونية، أدوات منزلية ذكية، ومنتجات تقنية حديثة",
                sortOrder = 1
            ),
            CategoryEntity(
                id = 2,
                nameAr = "مستلزمات التجميل",
                nameEn = "Cosmetics & Beauty",
                iconKey = "cosmetics",
                description = "عطور ملكية فاخرة، أدوات تجميل، ومنتجات عناية نسائية ورجالية",
                sortOrder = 2
            ),
            CategoryEntity(
                id = 3,
                nameAr = "مستلزمات الجوال",
                nameEn = "Mobile Accessories",
                iconKey = "phone",
                description = "شواحن سريعة، سماعات لاسلكية، كيابل، حوامل، وحافظات حماية",
                sortOrder = 3
            ),
            CategoryEntity(
                id = 4,
                nameAr = "الهدايا الفاخرة",
                nameEn = "Royal Gifts",
                iconKey = "gift",
                description = "بكجات هدايا فخمة للمناسبات الخاصة والهدايا الشخصية الراقية",
                sortOrder = 4
            )
        )
        db.categoryDao().insertCategories(categories)

        // 2. Products
        val products = listOf(
            ProductEntity(
                id = 1,
                name = "ساعة ذكية ملكية Ultra Pro شاشة AMOLED",
                categoryId = 1,
                subCategory = "ساعات ذكية",
                description = "ساعة ذكية فاخرة بهيكل من التيتانيوم وشاشة AMOLED تدعم الاتصال بالبلوتوث وتتبع نبضات القلب والأنشطة الرياضية وبطارية تدوم حتى 7 أيام.",
                specifications = "الشاشة: AMOLED 2.04 بوصة\nالبطارية: 450mAh تدوم 7 أيام\nالمقاومة: مقاومة للماء بمعيار IP68\nالاتصال: Bluetooth 5.3 + مكالمات\nالتوافق: Android و iOS",
                priceYer = 28000.0,
                priceSar = 190.0,
                originalPriceYer = 35000.0,
                discountPercent = 20,
                stockQuantity = 14,
                sku = "EL-WATCH-01",
                keywords = "ساعة ذكية شاشة الترا ساعة رياضية",
                isBestSeller = true,
                isNewArrival = true,
                isFeatured = true,
                hasSpecialOffer = true
            ),
            ProductEntity(
                id = 2,
                name = "سماعات رأس لاسلكية ANC عازلة للضوضاء الفائقة",
                categoryId = 1,
                subCategory = "صوتيات",
                description = "سماعات رأس احترافية فاخرة بعزل ضوضاء هجين وصوت نقي بدقة Hi-Res مع وسائد جلدية ناعمة راحة طوال اليوم.",
                specifications = "نوع العزل: Active Noise Cancelling (ANC)\nالبطارية: 40 ساعة تشغيل متواصل\nالشحن: Type-C سريع\nالميكروفون: 4 ميكروفونات للمكالمات النقية",
                priceYer = 32000.0,
                priceSar = 210.0,
                originalPriceYer = 40000.0,
                discountPercent = 20,
                stockQuantity = 8,
                sku = "EL-HEAD-02",
                keywords = "سماعة عازلة صوتيات سماعات راس بلوتوث",
                isBestSeller = true,
                isNewArrival = false,
                isFeatured = true,
                hasSpecialOffer = true
            ),
            ProductEntity(
                id = 3,
                name = "ماكينة قهوة وإسبريسو أوتوماتيكية 20 بار",
                categoryId = 1,
                subCategory = "أدوات منزلية إلكترونية",
                description = "ماكينة إعداد القهوة والإسبريسو والكابتشينو بقوة ضغط 20 بار مع مبخر حليب مدمج وهيكل ستانلس ستيل أنيق.",
                specifications = "الضغط: 20 Bar مضخة إيطالية\nسعة الخزان: 1.5 لتر قابل للإزالة\nالقوة: 1350 واط\nالميزات: تسخين سريع، رغوة حليب كثيفة",
                priceYer = 65000.0,
                priceSar = 420.0,
                originalPriceYer = 75000.0,
                discountPercent = 13,
                stockQuantity = 5,
                sku = "EL-COF-03",
                keywords = "قهوة اسبريسو ماكينة كابتشينو ادوات منزلية",
                isBestSeller = false,
                isNewArrival = true,
                isFeatured = true,
                hasSpecialOffer = false
            ),
            ProductEntity(
                id = 4,
                name = "بروجيكتور منزلي ذكي بدقة 4K بنظام أندرويد",
                categoryId = 1,
                subCategory = "منتجات تقنية",
                description = "بروجيكتور محمول سينمائي ذكي يدعم دقة 4K مع نظام أندرويد لتشغيل يوتيوب ونتفلكس وشاشة عرض حتى 150 بوصة.",
                specifications = "السطوع: 8000 لومن\nحجم العرض: 40 - 150 بوصة\nالنظام: Android 11 مدمج\nالمنافذ: HDMI, USB, Audio, WiFi 6",
                priceYer = 52000.0,
                priceSar = 340.0,
                originalPriceYer = 60000.0,
                discountPercent = 13,
                stockQuantity = 4,
                sku = "EL-PROJ-04",
                keywords = "بروجكتر سينما منزلية شاشة عرض بروجكتور",
                isBestSeller = false,
                isNewArrival = true,
                isFeatured = false,
                hasSpecialOffer = false
            ),
            ProductEntity(
                id = 5,
                name = "عطر ملكي فاخر 'دهن العود والمسك الملكي' 100ml",
                categoryId = 2,
                subCategory = "عطور",
                description = "توليفة ملكية حصرية تمزج بين دهن العود الكمبودي النادر والمسك الأبيض والعنبر الفاخر، ثبات يدوم أكثر من 48 ساعة وفخامة لا تضاهى.",
                specifications = "الحجم: 100 مل\nالتركيز: Eau de Parfum فاخر\nالمكونات: دهن عود، مسك ملكي، ورد بلغاري، عنبر\nالثبات: 48+ ساعة",
                priceYer = 22000.0,
                priceSar = 150.0,
                originalPriceYer = 28000.0,
                discountPercent = 21,
                stockQuantity = 22,
                sku = "BE-PERF-01",
                keywords = "عطر عود مسك ملكي عطور فخمة دهن عود",
                isBestSeller = true,
                isNewArrival = false,
                isFeatured = true,
                hasSpecialOffer = true
            ),
            ProductEntity(
                id = 6,
                name = "مجموعة العناية الذهبية 24K بالبشرة (سيروم + كريم + غسول)",
                categoryId = 2,
                subCategory = "مستلزمات العناية",
                description = "بكج العناية الكاملة بخلاصة جزيئات الذهب عيار 24 وحمض الهيالورونيك لنضارة وتجديد خلايا البشرة ومكافحة التجاعيد.",
                specifications = "المحتويات: سيروم ذهب 50 مل، كريم مغذي 60 جم، غسول منقي 100 مل\nالمكونات: جزيئات ذهب 24K، كولاجين، فيتامين E\nمناسب لجميع أنواع البشرة",
                priceYer = 18000.0,
                priceSar = 120.0,
                originalPriceYer = 24000.0,
                discountPercent = 25,
                stockQuantity = 12,
                sku = "BE-CARE-02",
                keywords = "عناية بالبشرة سيروم ذهب كريم ترطيب نضارة",
                isBestSeller = true,
                isNewArrival = true,
                isFeatured = true,
                hasSpecialOffer = true
            ),
            ProductEntity(
                id = 7,
                name = "حقيبة مكياج نسائية ملكية متكاملة 48 قطعة",
                categoryId = 2,
                subCategory = "أدوات تجميل",
                description = "حقيبة مكياج أنيقة مصممة للمناسبات تحتوي على ظلال عيون، أحمر شفاه، فرش احترافية، بودرة وإضاءة ثابتة عالية الجودة.",
                specifications = "عدد القطع: 48 قطعة متنوعة\nالمحتوى: درجات ظلال متعددة، أرواج مخملية، طقم فرش ناعم\nالعلبة: حقيبة جلدية فاخرة بمرآة مدمجة",
                priceYer = 26000.0,
                priceSar = 175.0,
                originalPriceYer = 32000.0,
                discountPercent = 19,
                stockQuantity = 9,
                sku = "BE-KIT-03",
                keywords = "مكياج حقيبة ارواج فرش تجميل نسائي",
                isBestSeller = false,
                isNewArrival = true,
                isFeatured = false,
                hasSpecialOffer = false
            ),
            ProductEntity(
                id = 8,
                name = "شاحن جداري GaN سريع بقوة 65W ثلاثي المنافذ",
                categoryId = 3,
                subCategory = "شواحن",
                description = "شاحن سريع معتمد بتقنية نيتريد الغاليوم (GaN) يدعم شحن اللابتوبات والهواتف الذكية مع منفذين Type-C ومنفذ USB-A.",
                specifications = "القدرة: 65 واط كحد أقصى\nالمنافذ: 2x USB-C PD 3.0 + 1x USB-A QC 4.0\nالحماية: حماية من الجهد الزائد وارتفاع الحرارة",
                priceYer = 14000.0,
                priceSar = 95.0,
                originalPriceYer = 18000.0,
                discountPercent = 22,
                stockQuantity = 25,
                sku = "MO-CHG-01",
                keywords = "شاحن سريع شاحن جوال تايب سي جان GaN",
                isBestSeller = true,
                isNewArrival = false,
                isFeatured = true,
                hasSpecialOffer = true
            ),
            ProductEntity(
                id = 9,
                name = "بنك طاقة باور بنك 30,000mAh شحن سريع 22.5W",
                categoryId = 3,
                subCategory = "شواحن وبطاريات",
                description = "باور بنك بسعة عملاقة 30 ألف ملي أمبير يدعم الشحن السريع مع شاشة ديجيتال لعرض النسبة بدقة و4 كابلات مدمجة.",
                specifications = "السعة: 30,000 mAh\nالقدرة: 22.5 واط Super Fast Charge\nالشاشة: LED رقمية لمعرفة النسبة\nالكابلات المدمجة: Type-C, Lightning, Micro-USB",
                priceYer = 19500.0,
                priceSar = 130.0,
                originalPriceYer = 25000.0,
                discountPercent = 22,
                stockQuantity = 18,
                sku = "MO-PWR-02",
                keywords = "باور بنك شاحن متنقل بطارية 30000 بنك طاقة",
                isBestSeller = true,
                isNewArrival = true,
                isFeatured = true,
                hasSpecialOffer = true
            ),
            ProductEntity(
                id = 10,
                name = "سماعة بلوتوث لاسلكية Pro مع شاشة ذكية في الحافظة",
                categoryId = 3,
                subCategory = "سماعات",
                description = "سماعات أذن لاسلكية ترو وايرلس بتقنية صوت ستيريو محيطي وحافظة مزودة بشاشة لمس ذكية للتحكم بالصوت والخلفيات.",
                specifications = "الشاشة: LCD لمس على الحافظة\nالبلوتوث: الإصدار 5.4 الأحدث\nالبطارية: 6 ساعات للسماعة + 28 ساعة بالحافظة\nالميزات: شاشة للرد على المكالمات وتغيير المعادل",
                priceYer = 16000.0,
                priceSar = 110.0,
                originalPriceYer = 20000.0,
                discountPercent = 20,
                stockQuantity = 15,
                sku = "MO-EAR-03",
                keywords = "سماعة ايربودز بلوتوث سماعات شاشة ذكية",
                isBestSeller = false,
                isNewArrival = true,
                isFeatured = true,
                hasSpecialOffer = false
            ),
            ProductEntity(
                id = 11,
                name = "حامل جوال مغناطيسي فاخر للسيارة مع شاحن لاسلكي MagSafe",
                categoryId = 3,
                subCategory = "حوامل واكسسوارات",
                description = "حامل سيارة بتثبيت قوي على فتحة المكيف أو الطبلون مع مغناطيس N52 فائق القوة وشحن لاسلكي سريع 15W.",
                specifications = "الشحن: لاسلكي بقدرة 15W\nالمغناطيس: مصفوفة مغناطيسية N52 مضادة للاهتزاز\nالدوران: 360 درجة لجميع الزوايا",
                priceYer = 9500.0,
                priceSar = 65.0,
                originalPriceYer = 12000.0,
                discountPercent = 20,
                stockQuantity = 20,
                sku = "MO-HLD-04",
                keywords = "حامل جوال حامل سيارة ماج سيف شاحن لاسلكي",
                isBestSeller = false,
                isNewArrival = false,
                isFeatured = false,
                hasSpecialOffer = false
            ),
            ProductEntity(
                id = 12,
                name = "بكج الهدية الملكية الرجالي الفاخر (ساعة + قلم + كبك وميدالية)",
                categoryId = 4,
                subCategory = "هدايا رجالية",
                description = "طقم ملكي مذهب متكامل معروض داخل صندوق جلدي مبطن بالمخمل الملكي، مثالي كهدية تخرج أو زواج أو تكريم شخصيات.",
                specifications = "المحتويات: ساعة كوارتز مقاومة للماء، قلم حبر سائل ذهبي، كبكات ستانلس مذهبة، ميدالية راقية\nالتغليف: بوكس ملكي أسود وذهبي فاخر مع كيس إهداء",
                priceYer = 35000.0,
                priceSar = 230.0,
                originalPriceYer = 45000.0,
                discountPercent = 22,
                stockQuantity = 7,
                sku = "GI-MEN-01",
                keywords = "طقم هدية بكج ملكي ساعة قلم كبك هدايا رجالية",
                isBestSeller = true,
                isNewArrival = true,
                isFeatured = true,
                hasSpecialOffer = true
            ),
            ProductEntity(
                id = 13,
                name = "صندوق هدية المناسبات الملكية (مصحف مذهب + مبخرة إلكترونية + دهن عود)",
                categoryId = 4,
                subCategory = "هدايا المناسبات",
                description = "هدية إيمانية وروحانية فاخرة تضم مصحفاً شريفاً بحواف مذهبة داخل صندوق خشبي منقوش يدوياً مع مبخرة ذكية متنقلة ودهن عود نقي.",
                specifications = "المحتوى: مصحف شريف فاخر مذهب، مبخرة إلكترونية قابلة للشحن، تولتين دهن عود معتق\nالصندوق: خشب طبيعي مع حفر ليزر ملكي",
                priceYer = 42000.0,
                priceSar = 280.0,
                originalPriceYer = 50000.0,
                discountPercent = 16,
                stockQuantity = 6,
                sku = "GI-QUR-02",
                keywords = "مصحف مذهب مبخرة دهن عود هدايا مناسبات صندوق خشبي",
                isBestSeller = false,
                isNewArrival = true,
                isFeatured = true,
                hasSpecialOffer = false
            )
        )
        db.productDao().insertProducts(products)

        // 3. Delivery Zones in Yemen
        val deliveryZones = listOf(
            DeliveryZoneEntity(id = 1, governorateName = "صنعاء", deliveryFeeYer = 3000.0, freeDeliveryThresholdYer = 50000.0, estimatedDays = "نفس اليوم أو خلال 24 ساعة"),
            DeliveryZoneEntity(id = 2, governorateName = "عدن", deliveryFeeYer = 5000.0, freeDeliveryThresholdYer = 60000.0, estimatedDays = "2-3 أيام"),
            DeliveryZoneEntity(id = 3, governorateName = "تعز", deliveryFeeYer = 4500.0, freeDeliveryThresholdYer = 60000.0, estimatedDays = "2-3 أيام"),
            DeliveryZoneEntity(id = 4, governorateName = "إب", deliveryFeeYer = 4000.0, freeDeliveryThresholdYer = 55000.0, estimatedDays = "1-2 أيام"),
            DeliveryZoneEntity(id = 5, governorateName = "الحديدة", deliveryFeeYer = 4500.0, freeDeliveryThresholdYer = 60000.0, estimatedDays = "2-3 أيام"),
            DeliveryZoneEntity(id = 6, governorateName = "حضرموت", deliveryFeeYer = 6000.0, freeDeliveryThresholdYer = 70000.0, estimatedDays = "3-4 أيام"),
            DeliveryZoneEntity(id = 7, governorateName = "ذمار", deliveryFeeYer = 3500.0, freeDeliveryThresholdYer = 50000.0, estimatedDays = "1-2 أيام"),
            DeliveryZoneEntity(id = 8, governorateName = "مأرب", deliveryFeeYer = 5000.0, freeDeliveryThresholdYer = 65000.0, estimatedDays = "2-3 أيام"),
            DeliveryZoneEntity(id = 9, governorateName = "لحج", deliveryFeeYer = 4500.0, freeDeliveryThresholdYer = 60000.0, estimatedDays = "2-3 أيام"),
            DeliveryZoneEntity(id = 10, governorateName = "البيضاء", deliveryFeeYer = 4500.0, freeDeliveryThresholdYer = 60000.0, estimatedDays = "2-3 أيام"),
            DeliveryZoneEntity(id = 11, governorateName = "صعدة", deliveryFeeYer = 5000.0, freeDeliveryThresholdYer = 60000.0, estimatedDays = "2-3 أيام"),
            DeliveryZoneEntity(id = 12, governorateName = "عمران", deliveryFeeYer = 3500.0, freeDeliveryThresholdYer = 50000.0, estimatedDays = "1-2 أيام")
        )
        db.deliveryZoneDao().insertZones(deliveryZones)

        // 4. Default Users (Super Admin, Staff, Customer)
        val users = listOf(
            UserEntity(
                id = 1,
                username = "ziko",
                name = "زكريا يحيى ناصر (المدير العام)",
                phone = "777128378",
                email = "admin@almalaki.ye",
                password = "ziko",
                governorate = "صنعاء",
                city = "صنعاء",
                addressDetails = "شارع حدة - برج المتجر الملكي",
                role = UserRole.SUPER_ADMIN
            ),
            UserEntity(
                id = 2,
                username = "sami",
                name = "سامي الكبسي (مسؤول الطلبات)",
                phone = "777111222",
                email = "orders@almalaki.ye",
                password = "staff",
                governorate = "صنعاء",
                city = "صنعاء",
                addressDetails = "مكتب تجهيز الطلبات",
                role = UserRole.ORDERS_STAFF
            ),
            UserEntity(
                id = 3,
                username = "waleed",
                name = "وليد اليافعي (مسؤول المخزون)",
                phone = "777333444",
                email = "stock@almalaki.ye",
                password = "stock",
                governorate = "صنعاء",
                city = "صنعاء",
                addressDetails = "مستودعات المتجر الملكي المركزية",
                role = UserRole.INVENTORY_STAFF
            ),
            UserEntity(
                id = 4,
                username = "customer",
                name = "محمد يحيى عبد الله",
                phone = "771234567",
                email = "customer@gmail.com",
                password = "user",
                governorate = "صنعاء",
                city = "حدة",
                addressDetails = "خلف فندق شيراتون - جوار سوبرماركت الهدى",
                role = UserRole.CUSTOMER
            )
        )
        db.userDao().insertUsers(users)

        // 5. Payment Methods (Dynamic & Admin-manageable)
        val paymentMethods = listOf(
            PaymentMethodEntity(
                id = 1,
                name = "الدفع عند الاستلام",
                type = PaymentType.COD,
                allowedGovernorates = "صنعاء,إب",
                instructions = "الدفع نقداً عند استلام الشحنة من مندوب التوصيل (متاح فقط داخل صنعاء أو إب)",
                sortOrder = 1
            ),
            PaymentMethodEntity(
                id = 2,
                name = "كريمي",
                accountNumber = "3175275141",
                accountHolder = "زكريا يحيى ناصر",
                currency = "YER",
                type = PaymentType.BANK_TRANSFER,
                instructions = "تحويل إلى حساب بنك الكريمي بالريال اليمني ثم إرفاق إشعار التحويل",
                sortOrder = 2
            ),
            PaymentMethodEntity(
                id = 3,
                name = "كريمي — حساب سعودي",
                accountNumber = "3175325618",
                accountHolder = "زكريا يحيى ناصر",
                currency = "SAR",
                type = PaymentType.BANK_TRANSFER,
                instructions = "تحويل إلى حساب بنك الكريمي بالريال السعودي ثم إرفاق إشعار التحويل",
                sortOrder = 3
            ),
            PaymentMethodEntity(
                id = 4,
                name = "محفظة جيب",
                accountNumber = "767650",
                accountHolder = "زكريا يحيى ناصر",
                currency = "YER",
                type = PaymentType.E_WALLET,
                instructions = "تحويل عبر محفظة جيب الإلكترونية ثم إرفاق لقطة شاشة لنجاح التحويل",
                sortOrder = 4
            ),
            PaymentMethodEntity(
                id = 5,
                name = "محفظة كاش",
                accountNumber = "715103209",
                accountHolder = "زكريا يحيى ناصر",
                currency = "YER",
                type = PaymentType.E_WALLET,
                instructions = "تحويل عبر محفظة كاش ثم إرفاق لقطة شاشة للعملية",
                sortOrder = 5
            ),
            PaymentMethodEntity(
                id = 6,
                name = "حوالة عبر العمقي",
                accountNumber = "777128378",
                accountHolder = "زكريا يحيى صالح صالح علي ناصر",
                currency = "YER",
                type = PaymentType.REMITTANCE,
                instructions = "إرسال حوالة عبر شبكة العمقي للصرافة باسم زكريا يحيى صالح صالح علي ناصر ورقم هاتف 777128378",
                sortOrder = 6
            ),
            PaymentMethodEntity(
                id = 7,
                name = "حوالة عبر أي صراف",
                accountNumber = "777128378",
                accountHolder = "زكريا يحيى صالح علي ناصر",
                currency = "YER",
                type = PaymentType.REMITTANCE,
                instructions = "إرسال حوالة عبر أي شبكة صرافة يمنية باسم زكريا يحيى صالح علي ناصر ورقم هاتف 777128378",
                sortOrder = 7
            )
        )
        db.paymentMethodDao().insertPaymentMethods(paymentMethods)

        // 6. Coupons
        val coupons = listOf(
            CouponEntity(id = 1, code = "ROYAL20", discountType = DiscountType.PERCENT, discountValue = 20.0, minOrderAmountYer = 10000.0, expiryDate = "2026-12-31"),
            CouponEntity(id = 2, code = "ALMALAKI", discountType = DiscountType.FIXED, discountValue = 5000.0, minOrderAmountYer = 25000.0, expiryDate = "2026-12-31"),
            CouponEntity(id = 3, code = "WELCOME", discountType = DiscountType.PERCENT, discountValue = 10.0, minOrderAmountYer = 5000.0, expiryDate = "2026-12-31")
        )
        db.couponDao().insertCoupons(coupons)

        // 7. Initial Sample Orders (to populate Admin Dashboard metrics & customer order history)
        val sampleOrder1 = OrderEntity(
            id = 1,
            orderNumber = "#RM-10001",
            customerId = 4,
            customerName = "محمد يحيى عبد الله",
            customerPhone = "771234567",
            governorate = "صنعاء",
            city = "حدة",
            addressDetails = "شارع حدة جوار بنك اليمن الدولي",
            notes = "يرجى الاتصال قبل الوصول",
            subtotalYer = 46000.0,
            deliveryFeeYer = 3000.0,
            discountAmountYer = 5000.0,
            totalYer = 44000.0,
            status = OrderStatus.DELIVERED,
            paymentMethodId = 1,
            paymentMethodName = "الدفع عند الاستلام",
            paymentStatus = PaymentStatus.VERIFIED,
            createdAt = System.currentTimeMillis() - 86400000L * 2
        )
        db.orderDao().insertOrder(sampleOrder1)
        db.orderDao().insertOrderItems(
            listOf(
                OrderItemEntity(id = 1, orderId = 1, productId = 1, productName = "ساعة ذكية ملكية Ultra Pro شاشة AMOLED", productSku = "EL-WATCH-01", priceAtPurchase = 28000.0, quantity = 1, totalPrice = 28000.0),
                OrderItemEntity(id = 2, orderId = 1, productId = 6, productName = "مجموعة العناية الذهبية 24K بالبشرة", productSku = "BE-CARE-02", priceAtPurchase = 18000.0, quantity = 1, totalPrice = 18000.0)
            )
        )

        val sampleOrder2 = OrderEntity(
            id = 2,
            orderNumber = "#RM-10002",
            customerId = 4,
            customerName = "محمد يحيى عبد الله",
            customerPhone = "771234567",
            governorate = "عدن",
            city = "المنصورة",
            addressDetails = "الشارع الرئيسي قرب بنك القطيبي",
            notes = "تأكيد فوري عبر واتساب",
            subtotalYer = 35000.0,
            deliveryFeeYer = 5000.0,
            discountAmountYer = 0.0,
            totalYer = 40000.0,
            status = OrderStatus.SHIPPING,
            paymentMethodId = 2,
            paymentMethodName = "كريمي",
            paymentStatus = PaymentStatus.VERIFICATION_PENDING,
            paymentTransactionNumber = "KRM-99881122",
            paymentDate = System.currentTimeMillis() - 3600000L * 5,
            createdAt = System.currentTimeMillis() - 3600000L * 5
        )
        db.orderDao().insertOrder(sampleOrder2)
        db.orderDao().insertOrderItems(
            listOf(
                OrderItemEntity(id = 3, orderId = 2, productId = 12, productName = "بكج الهدية الملكية الرجالي الفاخر", productSku = "GI-MEN-01", priceAtPurchase = 35000.0, quantity = 1, totalPrice = 35000.0)
            )
        )

        val sampleOrder3 = OrderEntity(
            id = 3,
            orderNumber = "#RM-10003",
            customerId = 4,
            customerName = "هشام العزي",
            customerPhone = "775554433",
            governorate = "إب",
            city = "الظهار",
            addressDetails = "الدائري الغربي عمارة السلام",
            notes = "الطلب قيد المراجعة",
            subtotalYer = 41500.0,
            deliveryFeeYer = 4000.0,
            discountAmountYer = 0.0,
            totalYer = 45500.0,
            status = OrderStatus.NEW,
            paymentMethodId = 4,
            paymentMethodName = "محفظة جيب",
            paymentStatus = PaymentStatus.VERIFICATION_PENDING,
            paymentTransactionNumber = "JB-778899",
            paymentDate = System.currentTimeMillis() - 1800000L,
            createdAt = System.currentTimeMillis() - 1800000L
        )
        db.orderDao().insertOrder(sampleOrder3)
        db.orderDao().insertOrderItems(
            listOf(
                OrderItemEntity(id = 4, orderId = 3, productId = 5, productName = "عطر ملكي فاخر 'دهن العود والمسك الملكي' 100ml", productSku = "BE-PERF-01", priceAtPurchase = 22000.0, quantity = 1, totalPrice = 22000.0),
                OrderItemEntity(id = 5, orderId = 3, productId = 9, productName = "بنك طاقة باور بنك 30,000mAh", productSku = "MO-PWR-02", priceAtPurchase = 19500.0, quantity = 1, totalPrice = 19500.0)
            )
        )

        // 7. Initial Activity Log
        val logs = listOf(
            ActivityLogEntity(id = 1, userId = 1, userName = "المدير العام", actionType = "تهيئة النظام", details = "تم تشغيل وتفعيل متجر المتجر الملكي ALMALAKI STORE بنجاح."),
            ActivityLogEntity(id = 2, userId = 1, userName = "المدير العام", actionType = "إضافة منتجات", details = "تم إدراج المنتجات الملكية وتعيين أسعار الريال اليمني والسعودي."),
            ActivityLogEntity(id = 3, userId = 2, userName = "سامي الكبسي", actionType = "تحديث طلب", details = "تم نقل حالة الطلب #RM-10001 إلى تم التسليم.")
        )
        for (log in logs) {
            db.activityLogDao().insertLog(log)
        }

        // 8. Welcome Notification
        db.notificationDao().insertNotification(
            NotificationEntity(
                id = 1,
                userId = 4,
                title = "مرحباً بك في المتجر الملكي 👑",
                message = "يسعدنا انضمامك إلى المتجر الملكي. استمتع بتجربة تسوق فاخرة وعروض حصرية.",
                type = "PROMO"
            )
        )
    }
}
