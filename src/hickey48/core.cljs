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

(defn slices-of [size total]
  (partition size (range total))
  (loop [slices [] r (range 0 total)]
    (if (empty? r)
      slices
      (recur (conj slices (take size r)) (drop size r)))))

(defn get-all [seq keys]
  (for [k keys] (get seq k)))


(defn shift-group [vals]
  (println "shift group of: " vals)
  [])

(defn shift-board [dir board]
  ;; 2 2 0 0 -> 4 0 0 0
  (let [row-size (sqrt (count board))
        row-indices (partition row-size (range (count board)))]
    (for [s row-indices]
      (shift-group (get-all board s))))
  {})

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

