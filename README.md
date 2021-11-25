# Android Paging Sample
The sample shows how to implement a simple activity feed. The feed should implement fetch as you scroll with a page size of two weeks at a time. If the app doesn't receive any events within that time period it should fetch the next period until we get some events or we reach the oldest activity.

## Implementation
- The app relies on RxJava.
For Coroutines implementation, please refer to [Coroutines feature branch](https://github.com/dfavaro/android-pagination-sample/tree/feature/coroutines)
- Multi-Module app relying on a network module ([Retrofit](https://square.github.io/retrofit/)) and a local storage module ([Room](https://developer.android.com/jetpack/androidx/releases/room))
- [Jetpack Compose](https://developer.android.com/jetpack/compose) is used to display the list of items.
- A simple mocked API is available to get the list of activities and information about them.

#### Endpoint
http://qapital-ios-testtask.herokuapp.com

#### Fetch activities:
```
http://qapital-ios-testtask.herokuapp.com/activities?from=<date>&to=<date>
```

Where the two dates should be of the format YYYY-MM-DDThh:mm:ss+00:00. This will fetch all activities that happened between from and to. The return format is a JSON object that holds the list of activities under the key "activities" and a date indicating what is the earliest date there is an activity for under the key "oldest". The activities contain a message that has a number of words marked with `<strong>` that should be emphasized. The amount is in dollars. The user id can be used to fetch a user from the users endpoint.
```json
{
	"oldest": "2016-05-23T00:00:00+00:00",
	"activities": [
		{
			"message": "<strong>You</strong> didn't resist a guilty pleasure at <strong>Starbucks</strong>.",
			"amount": 2.5,
			"userId": 2,
			"timestamp": "2016-10-04T00:00:00+00:00"
		},
		{
			"message": "<strong>You</strong> made a roundup.",
			"amount": 0.32,
			"userId": 3,
			"timestamp": "2016-10-03T00:00:00+00:00"
		}
	]
}
```

#### Fetch a user:
```
http://qapital-ios-testtask.herokuapp.com/users/<id>
```

Returns the user with the specified id
```json
[
	{
		"userId": 1,
		"displayName": "Mikael",
		"avatarUrl": "http://qapital-ios-testtask.herokuapp.com/avatars/mikael.jpg"
	}
]
```
