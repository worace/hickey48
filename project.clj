(defproject hickey48 "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-3211"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                [figwheel "0.2.7-SNAPSHOT"]
                 [reagent "0.5.0-alpha3"]
                 ]

  :plugins [[lein-cljsbuild "1.0.5"]
            [lein-figwheel "0.3.3"]
            [figwheel "0.2.7-SNAPSHOT"]
            ]

  :source-paths ["src"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :cljsbuild {
    :builds [{:id "dev"
              :source-paths ["src"]

              :figwheel { :on-jsload "hickey48.core/on-js-reload" }

              :compiler {:main hickey48.core
                         :asset-path "js/compiled/out"
                         :output-to "resources/public/js/compiled/hickey48.js"
                         :output-dir "resources/public/js/compiled/out"
                         :source-map-timestamp true }}


             {:id "test"
              :source-paths ["src" "test/cljs"]
              :compiler {:output-to "resources/public/js/compiled/test/test.js"
                         :output-dir "resources/public/js/compiled/test/out"
                         :optimizations :none
                         :main hickey48.core-test
                         :asset-path "js/compiled/test/out"
                         :source-map true
                         :cache-analysis true }}]}

  :figwheel {
             ;; :http-server-root "public" ;; default and assumes "resources"
             ;; :server-port 3449 ;; default
             :css-dirs ["resources/public/css"] ;; watch and update CSS

             ;; Start an nREPL server into the running figwheel process
             ;; :nrepl-port 7888

             ;; Server Ring Handler (optional)
             ;; if you want to embed a ring handler into the figwheel http-kit
             ;; server, this is for simple ring servers, if this
             ;; doesn't work for you just run your own server :)
             ;; :ring-handler hickey48.server/handler

             ;; To be able to open files in your editor from the heads up display
             ;; you will need to put a script on your path.
             ;; that script will have to take a file path and a line number
             ;; ie. in  ~/bin/myfile-opener
             ;; #! /bin/sh
             ;; emacsclient -n +$2 $1
             ;;
             ;; :open-file-command "myfile-opener"

             ;; if you want to disable the REPL
             ;; :repl false

             ;; to configure a different figwheel logfile path
             ;; :server-logfile "tmp/logs/figwheel-logfile.log" 
             })
