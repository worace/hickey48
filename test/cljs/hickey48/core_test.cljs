(ns ^:figwheel-always hickey48.core-test
  (:require
    [cljs.test :refer-macros [deftest testing is]]
    [hickey48.test-formatter :as formatter]
    [figwheel.client :as fw]
    [dommy.core :refer-macros [sel sel1]]
    [hickey48.test-helpers :as th]
    [hickey48.core :as c]))

(enable-console-print!)

(deftest test-renders-board
  (is (= 16 (count (sel ".square")))))

(deftest test-sets-and-gets-vals
  (c/set-value! 5 20)
  (is (= 20 (c/get-value 5))))

(deftest test-board-renders-proper-vals
  (c/set-value! 3 999)
  (is (= 999 (c/get-value 3)))
  (is (th/found-in #"999" (sel1 "#square-3"))))

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
