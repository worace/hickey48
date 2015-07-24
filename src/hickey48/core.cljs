(ns ^:figwheel-always hickey48.core
    (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(defn sqrt [n] (.sqrt js/Math n))
(defn starter-val [] (rand-nth [2 4]))
(defn starter-positions [board-size]
  (zipmap (take 2 (shuffle (range 0 16)))
          (take 2 (repeatedly starter-val))))
(defn blank-board [board-size]
  (into (sorted-map) (for [i (range 0 board-size)] {i 0})))

(defn gen-board [board-size]
    (merge (blank-board board-size) (starter-positions board-size)))

(defn initial-state [] {:text "Hello world!" :board (gen-board 16)})

(defonce app (atom (initial-state)))

(defn revert! [] (swap! app initial-state))

(defn set-value! [idx val]
  (swap! app update-in [:board] assoc idx val))

(defn get-value [idx] (get-in @app [:board idx]))

(defn get-all [seq keys]
  (println "getting all " keys " from " seq)
  (for [k keys] (get seq k)))

(defn padded [n coll]
  (take n (concat coll (repeat 0))))

(defn paired [coll]
  (loop [pairs [] candidate (first coll) coll (rest coll)]
    (if (empty? coll)
      (if candidate (conj pairs [candidate]) pairs)
      ;; if there is a candidate and its same as me, make a pair and conj to pairs
      ;; otherwise conj that candidate as a solo group and make me new candidate
      (if (= candidate (first coll))
        (recur (conj pairs [candidate (first coll)]) (second coll) (drop 2 coll))
        (recur (conj pairs [candidate]) (first coll) (rest coll))))))

(def non-zero? (partial filter (comp not zero?)))

(defn shift-group [vals]
  (padded (count vals)
          (map (partial reduce +)
               (paired (non-zero? vals)))))

(defn shift-board [dir board]
  ;; 2 2 0 0 -> 4 0 0 0
  (let [row-size (sqrt (count board))
        row-keys (partition row-size (range (count board)))]
    (map (fn [key-slice]
           (shift-group (get-all board key-slice)))
         row-keys)))

;; (map (fn [r]
;;        (println "mapping")
;;        (shift-group (get-all board r)))
;;      row-indices)
;; (for [s (map (fn [r] (get-all board r)) row-indices)]
;;   (println (get-all board s))
;;   #_(shift-group (get-all board s)))
(defn square [[id value]]
  [:div {:class "square" :id (str "square-" id)} [:p value]])

(defn root []
   [:div {:id "game"} (map square (@app :board))])

(reagent/render-component [root]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

