# Yelp Clone 

## *Dean Stratakos*

**Yelp clone** displays a list of search results from the Yelp API and displays the results in a scrollable list. 

Time spent: **20** hours spent in total

## Functionality 

The following **required** functionality is completed:

* [x] Ability to query the Yelp API to get results from a search query
* [x] The search results are displayed in a RecyclerView

The following **extensions** are implemented:

* [x] Clicking on a business in the list opens a new page with more details
    * [x] A tab layout is implemented with Overview and Reviews tabs
    * [x] In the Overview tab photos are displayed in a image slider component
* [x] The search bar allows a user to query for new restaurants. The user can also specify location
    by using the word "in" (e.g. "breakfast in Marin").
* [x] The app icon and color scheme are customized to resemble the official Yelp app

## Video Walkthrough

### Design

The first GIF demonstrates the app logo as well as the design of the app's home page with the Yelp
color scheme. The restaurant list shows basic information about each restaurant along with a
corresponding thumbnail.

<img src="GIFs/design.gif" title="App design" alt="App design" width="400" />

### Search feature

Below is a walkthrough of the search feature. A user can type a category in the search bar and the
list of restaurants will update based on the key words. Furthermore, the user can specify a location
by typing "`category` in `location`". The list then updates to show the relevant restaurants in that
location. Then, the user can enter a new search, and the previous location will be remembered and
used until the user enters a new location. Note that the original list of pizzas is different from
the list of pizzas after searching for "sandwiches in Palo Alto".

<img src="GIFs/search.gif" title="Search feature" alt="Search feature" width="400" />

### Details page

This GIF displays the restaurant detail activity. The restaurant's main photo is displayed at the
top, and there are two tabs underneath - Overview and Reviews. In the Overview tab, the user can
see detailed information about the restaurant including the phone number and types of transactions.
The Overview tab also includes an image slider to display other photos of the restaurant. The image
slider automatically transitions every 4 seconds, but the user can also manually swipe to view more
images. In the Review tab, the user can see recent reviews on the restaurant along with the name of
the reviewer and the date of the review.

<img src="GIFs/details.gif" title="Restaurant details" alt="Restaurant details" width="400" />

## Notes

The biggest challenge came when implementing the tab system. I used a TabLayout coupled with a
ViewPager as the main components. Then, I created separate layouts for each of the tabs and
managed them through a PagerAdapter. It was also difficult to combine this with the Yelp API calls
to make sure that the components were inflated before trying to populate them with the Yelp search
results. I have a working solution now, but there are still some bugs. Specifically, I get the error
```
lateinit property adapter has not been initialized
```
once in a while when clicking on a restaurant in the list. I believe this doesn't occur all the time
because the inflater is usually faster than the Yelp API call. This is an area of future improvement.

Another challenge was creating a search bar. Initially, I had created a custom toolbar to replace the
default menu. The custom toolbar allowed me to specify the text in the title bar as well as include
a custom image - the Yelp logo. However, this created huge complications when trying to add on a
search view. I ended up removing the custom toolbar and reverting back to the default title bar
because I decided that the search bar took precedence. In the future, I could implement two search
bars - one for category and one for location - perhaps by having two EditText views. However, I
already have a decent solution of allowing the user to input just "`category`" or to specify
"`category` in `location`".

To create the image slider, I imported a library. This was great as it had readily available features
and animations, but it was less customizable than if I had implemented it from scratch.

## License

    Copyright 2020 Dean Stratakos

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
