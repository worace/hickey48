(ns ^:figwheel-always hickey48.core
    (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(defn starter-val [] (rand-nth [2 4]))
(defn starter-positions [board-size]
  (zipmap (take 2 (shuffle (range 0 16)))
          (take 2 (repeatedly starter-val))))

(defn gen-board [board-size]
  (let [blank-board (into {} (for [i (range 0 board-size)] {i 0}))]
    (merge blank-board (starter-positions board-size))))

(defn initial-state [] {:text "Hello world!" :board (gen-board 16)})

(defonce app (atom (initial-state)))

(defn revert! [] (swap! app initial-state))

(defn set-value! [idx val]
  (swap! app update-in [:board] assoc idx val))

(defn get-value [idx] (get-in @app [:board idx]))

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

