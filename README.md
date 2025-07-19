  Product Shopping App

- MVI architecture, but actions and intents aren't fully integrated to make it complelety MVI.
  The AddToCart and update/clear cart methods aren't included in the ProductsIntent.
  - Room necessary for local persistence and offline viewing.
  - Could have moved many of the strings to the strings folder.
  - The data is initially retrieved from the assets folder in the project storage then written to internal storage. A mock service is used for retrieving and updating this file stored internally to the application. The contents of this file are read and updated in the json interceptor for the service by checking request type/url segments and parsing accordingly. After the services are called for getting the data, updating and clearing, the same changes are made to the local db storage.
  - No paging, just infinate scroll.
  - Deeplinking is implemented, you can try running;

adb shell am start -a android.intent.action.VIEW -d "app://productshopping/productsHome"
adb shell am start -a android.intent.action.VIEW -d "app://productshopping/productDetail/123"
adb shell am start -a android.intent.action.VIEW -d "app://productshopping/shoppingCart"

from your terminal window to see that the screens show up on your phone/emulator.

Features
- Viewing the products
- Seeing their details after selecting one.
- Viewing the cart.
- Adding a quantity of the product (depending on inventory max) to the cart.
- Modifying cart quantities.
- Resetting the cart.
- Viewing total prices of each item with quantity, or total cart value.

Technologies;
- MVVM, Jetpack Compose, Room, Retrofit2, Hilt for di, OkHttp3 for logging interceptor, coroutines, mockk for testing.

  
