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

(deftest test-generating-starter-vals
  (is (= 4 (count (c/starter-positions 16)))))

(deftest test-makes-blank-board
  (is (= [0 0 0 0 0 0] (c/blank-board 6))))

(deftest test-starts-with-some-squares-filled
  (c/revert!)
  (is (> (reduce + (@c/app :board)) 2)))

(deftest test-sets-and-gets-vals
  (c/set-value! 5 20)
  (is (= 20 (c/get-value 5))))

(deftest test-board-renders-proper-vals
  (c/set-value! 3 999)
  (is (= 999 (c/get-value 3)))
  (is (th/found-in #"999" (sel1 "#square-3"))))

(deftest test-pairing-values
  (is (= [[2 2] [2]] (c/paired [2 2 2])))
  (is (= [[2 2]] (c/paired [2 2])))
  (is (= [[4] [2 2] [8]] (c/paired [4 2 2 8])))
  )

(deftest test-shifting-groups
  (is (= [4 0 0 0] (c/shift-group [2 2 0 0])))
  (is (= [4 0 0 0] (c/shift-group [2 0 0 2])))
  (is (= [16 0 0 0] (c/shift-group [8 0 8 0])))
  (is (= [8 4 0 0] (c/shift-group [4 0 4 4])))
  )

(deftest test-shifting-left
  (let [b (apply assoc (c/blank-board 16) [0 2 1 2])
        shifted (c/shift-board :left b)]
    (is (= [4 0 0 0] (take 4 shifted))))
  (let [b (apply assoc (c/blank-board 16) [0 4 1 0 2 2 3 2])
        shifted (c/shift-board :left b)]
    (is (= [4 4 0 0] (take 4 shifted)))))

(deftest test-shifting-right
  (let [b (apply assoc (c/blank-board 16) [0 2 1 2])
        shifted (c/shift-board :right b)]
    (is (= [0 0 0 4] (take 4 shifted)))))

(deftest test-shifting-up
  (let [b (apply assoc (c/blank-board 16) [1 2 5 2])
        shifted (c/shift-board :up b)]
    (is (= [0 4 0 0] (take 4 shifted)))))

(deftest test-shifting-down
  (let [b (apply assoc (c/blank-board 16) [1 2 5 2])
        shifted (c/shift-board :down b)]
    (println "start board: " (clojure.string/join "\n" (partition 4 b)))
    (println "shifted: " (clojure.string/join "\n" (partition 4 shifted)))
    (is (= [0 4 0 0] (take 4 (drop 12 shifted))))))

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
