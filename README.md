  Product Shopping App

- MVI architecture, but actions and intents aren't fully integrated to make it complelety MVI.
  The AddToCart and update/clear cart methods weren't included in the ProductsIntent.
  - Room necessary for local persistence and offline viewing.
  - If there was a full REST service I'd have made the data refreshable for the shopping cart.
  - Could have moved many of the strings to the strings folder.
  - A mock service is used for getting the local json file (stored in the assets folder). I could have written the data to a file somewhere in internal storage and read/wrote with that. That would have allowed me to update/delete/insert the data there. Instead I opted for retrieval of the data on the home and detail screen.
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

  
