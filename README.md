# clojurecart

start with:
lein ring server-headless

open in a browser:
http://localhost:3000

Couchdb should run on:
http://localhost:5984

databases users and carts will be created at first access

in the database carts the following document is needed:

	{
		"_id":"_design/carts",
		"views":{
			"all":{"map":"function(doc) { emit(null, doc) }"},
			"by_user":{"map":"function(doc) { emit(doc.uid, doc) }"}
		},
		"language":"javascript"
	}

## Prerequisites

You will need [Leiningen][1] 1.7.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2013 FIXME
