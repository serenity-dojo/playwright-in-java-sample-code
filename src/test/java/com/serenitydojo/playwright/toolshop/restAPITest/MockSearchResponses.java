package com.serenitydojo.playwright.toolshop.restAPITest;

public class MockSearchResponses {

    public static final String RESPONSE_WITH_A_SINGLE_ENTRY = """
            {
              "current_page": 1,
              "data": [
                {
                  "id": "01JFG8Q5XKZJY4BEYQ87PC2Q1Y",
                  "name": "Super Pliers",
                  "description": "Lorum ipsum",
                  "price": 9.99,
                  "is_location_offer": 1,
                  "is_rental": 0,
                  "in_stock": 0,
                  "co2_rating": "A",
                  "is_eco_friendly": true,
                  "brand": {
                    "id": "string",
                    "name": "new brand",
                    "slug": "new-brand"
                  },
                  "category": {
                    "id": "string",
                    "parent_id": "string",
                    "name": "new category",
                    "slug": "new-category",
                    "sub_categories": [
                      "string"
                    ]
                  },
                  "product_image": {
                    "by_name": "string",
                    "by_url": "string",
                    "source_name": "string",
                    "source_url": "string",
                    "file_name": "string",
                    "title": "string",
                    "id": "string"
                  }
                }
              ],
              "from": 1,
              "last_page": 1,
              "per_page": 1,
              "to": 1,
              "total": 1
            }
            """;


}
