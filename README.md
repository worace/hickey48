### Hickey48 - 2048 in Clojurescript

Using this app to experiment with more clojurescript. Using
Reagent for rendering the UI via React and `cljs.test` for testing.

#### To Run

The project includes 2 builds -- `dev` for building just the app itself
and `test` for the test suite.

Run just dev with:

```
lein figwheel dev
```

or just test:

```
lein figwheel test
```

or both:

```
lein figwheel dev test
```

View the app at `http://localhost:3449/` and the tests at `http://localhost:3449/test.html`.

(`3449` is the default port, but this is configurable under the
`:figwheel` settings in `project.clj`)

__Test Display__

Currently test output is just displayed in the JS dev console. Maybe
will add some HTML display at some point.

### Credit

Thanks to @bhauman for figwheel! Most of the ideas in this example were
cobbled together from his [crashverse](https://github.com/bhauman/crashverse) example
or from various figwheel readmes I found.
