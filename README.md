# themoviedb
App to show movies infos from 'themoviedb.org'

======= Technical considerations

Native android app developed in java
Min SDK 19
It supports both device orientations (portrait and Landscape)
It supports tablets and phone, with different layouts for each one. Split screen for tablets and separated screens to phone

External libraries:
 - Dagger 2: Creates, manages and inject data access classes in the application
 - Retrofit 2: Manages the communication with server
 - Jackson: Parse json from server
 - Realm: To persist search suggestions
 - Butterknife: Bind views
 - Picasso: To download and cache images from server

======= Approach

Data access layer.

The data access layer are composed for 3 main classes:
 - LocalStore: It's responsible to manage the local data base. In these case the app are using Realm as data base. We can change the data base to any another, modifying only this class, with no impact on the project structure.
 - NetworkApi: It's responsible to mange the remote data access, communicating with the server using Retrofit.
 - DataProvider: This class manage the other two (NetworkApi and LocalStore) and provide data to the application. The rest of application never know if the data came from local db or from server.

Screens layer.

For the screens was used fragments, to made the app modular and make possible different layouts to tablet and phone.
Main classes:
 - SearchListFragment: It's responsible for perform the search (communicating with DataProvider class) and shows the result
 - DetailsFragment: It's responsible for shows more information about the movie
 - MainActivity: Responsible for communication and navigation between both fragments
