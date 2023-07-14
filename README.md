# Comic Scroll

Comic Scroll is an Android app that displays the latest 100 [XKCD comics](https://xkcd.com/) in a scrollable view. It makes use of the [XKCD JSON API](https://xkcd.com/json.html).

<img src="art/sample.gif" width="320px" alt="Trending artwork home"/>

## Architecture

The app follows the [official architecture guidelines](https://developer.android.com/topic/architecture) as closely as possible. Separating concerns into two layers: a [data](https://developer.android.com/topic/architecture/data-layer) and [UI layer](https://developer.android.com/topic/architecture/ui-layer).  [Kotlin Flows](https://developer.android.com/kotlin/flow) are utilized for facilitating a unidirectional data flow between the layers.

### Paging Data

To display the 100 latest comics in a scroll view, the API design only offers getting a single comic per request. Instead of fetching all 100 comics at once, incremental fetching of the data is done using pagination. This approach offers more efficient usage of network and system resources while providing users with a smoother scrolling experience. The app utilizes the [Jetpack Paging library](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) to orchestrate the fetching of data as the view is scrolled. 

### Dependencies

A brief summary of the dependencies utilized in the app:

- [Kotlin Coroutines](https://developer.android.com/kotlin/coroutines) are used to handle the asynchronous code. 
- [Kotlin Serialization](https://kotlinlang.org/docs/serialization.html) for deserializing JSON into objects.
- [Lifecycle ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) for persisting and exposing UI state.
- [Navigation Components](https://developer.android.com/guide/navigation/navigation-getting-started) for adhering to Android's [set of principles](https://developer.android.com/guide/navigation/navigation-principles).
- [Paging](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) for loading comics gradually.
- [Retrofit](https://square.github.io/retrofit/) for handling HTTP API calls.
- [OkHttp](https://square.github.io/okhttp/) as the app's HTTP client.
- [Hilt](https://dagger.dev/hilt/) for dependency injection.
- [Coil](https://github.com/coil-kt/coil) for image loading.
- [Kotest Assertions](https://kotest.io/docs/assertions/assertions.html) for writing test assertions using Kotlin infix.

## Future Improvements

- Introduce LocalDataSources to cache data using [Room](https://developer.android.com/training/data-storage/room). To coordinate where data is loaded, `XKCDPagingSource` would need to migrate to implementing [`RemoteMediator`](https://developer.android.com/topic/libraries/architecture/paging/v3-network-db).
- Additional unit tests for `XKCDPagingSource` to cover edge cases.
- Utilize Coil's preload image functionality to improve UX.
- Improve app UI through Material theme customization and view layout tweaks.
