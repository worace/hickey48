(ns ^:figwheel-always hickey48.core-test
  (:require
    [cljs.test :refer-macros [deftest testing is]]
    [hickey48.test-formatter :as formatter]
    [figwheel.client :as fw]
    [hickey48.test-helpers :as th]
    [hickey48.core :as hw]))

(enable-console-print!)

(deftest test-pizza
  (is (= "pizza" "pizza")))

(deftest test-four
  (is (= 1 1)))

(deftest test-tacos
  (is (= 1 1)))

(deftest test-hickey48
  (is (th/found-in #"Hello" (th/by-id "app"))))

(defn run-tests []
  (.clear js/console)
  (cljs.test/run-all-tests #"hickey48.*-test"))
(run-tests)

;; FW connection is optional in order to simply run tests,
;; but is needed to connect to the FW repl and to allow
;; auto-reloading on file-save
(fw/start {
           :websocket-url "ws://localhost:3449/figwheel-ws"
           ;; :autoload false
           :build-id "test"
           })
