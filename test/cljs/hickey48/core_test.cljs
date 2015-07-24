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

(deftest test-roots (is (= 9 (c/sqrt 81))))

(deftest test-get-all
  (is (= ["c" "d"] (c/get-all {"a" "c" "b" "d"} ["a" "b"])))
  (is (= [1 2] (c/get-all [1 2] [0 1]))))

(deftest test-slices-of
  (is (= [[0 1] [2 3]] (c/slices-of 2 4)))
  (is (= [[0 1 2 3]] (c/slices-of 4 4)))
  (is (= [[0 1] [2 3] [4 5] [6 7]] (c/slices-of 2 8))))

(deftest test-sets-and-gets-vals
  (c/set-value! 5 20)
  (is (= 20 (c/get-value 5))))

(deftest test-board-renders-proper-vals
  (c/set-value! 3 999)
  (is (= 999 (c/get-value 3)))
  (is (th/found-in #"999" (sel1 "#square-3"))))

(deftest test-starts-with-some-squares-filled
  (c/revert!)
  (is (> (reduce + (vals (@c/app :board))) 2)))

(deftest test-shifting-left
  (let [b (merge (c/blank-board 16) {0 2 1 2})
        shifted (c/shift-board :left b)]
    (is (= 4 (shifted 4)))))

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
