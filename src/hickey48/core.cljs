(ns ^:figwheel-always hickey48.core
    (:require [reagent.core :as reagent :refer [atom]]
              [goog.events :as events]))

(enable-console-print!)

(defn sqrt [n] (.sqrt js/Math n))
(defn starter-val [] (rand-nth [2 4]))
(defn starter-positions [board-size]
  "Return a sequence of random indices and starter values
   where indices range the size of the board and starter vals
   are either 2 or 4. e.g. (15 2 3 4) -> index 15 is 2, 3 is 4"
  (interleave (take 2 (shuffle (range 0 board-size)))
              (take 2 (repeatedly starter-val))))

(defn blank-board [board-size] (vec (take board-size (repeat 0))))

(defn gen-board [board-size]
    (apply assoc (blank-board board-size) (starter-positions board-size)))

(defn initial-state [] {:text "Hello world!" :board (gen-board 16)})

(defonce app (atom (initial-state)))

(defn revert! [] (swap! app initial-state))

(defn set-value! [idx val] (swap! app update-in [:board] assoc idx val))

(defn get-value [idx] (get-in @app [:board idx]))

(defn padded [n coll] (take n (concat coll (repeat 0))))

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

(defn transpose [matrix]
  (apply map vector matrix))

;; shifting in dirs
;; idea:
;; identify a series of transformations for each
;; row that can convert it into the proper series for shifting
;; maintain those transformations as lists so we can apply
;; them forward and backward
;; apply the list forward to get rows and shift
;; then apply the list backward to revert to original series
;;
;; left - identity
;; right - reverse
;; up - transpose
;; down - transpose, then reverse rows

(defn shift-board [dir board]
  (let [row-size (sqrt (count board))
        rows (partition row-size board)]
    (vec (case dir
      :left (mapcat identity (map shift-group rows))
      :right (mapcat reverse (map shift-group (map reverse rows)))
      :up (mapcat identity (transpose (map shift-group (transpose rows ))))
      :down (mapcat identity (transpose (map reverse (map shift-group (map reverse (transpose rows))))))
      ))))

(defn square [id value]
  [:div {:class "square" :id (str "square-" id)} [:p value]])

(defn root []
   [:div {:id "game"} (map-indexed square (@app :board))])

(reagent/render-component [root]
                          (. js/document (getElementById "app")))


(def key-map
  {37 :left 38 :up 39 :right 40 :down
   ;; w - 87 a - 65 s - 83 d - 68
   87 :up 65 :left 83 :down 68 :right})

(def update-board! (partial swap! app assoc :board))

(defn with-randoms [board]
  (let [empty-positions (filter (comp not nil?) (for [i (range (count board))] (if (zero? (board i)) i)) )]
    (assoc board (first (shuffle empty-positions)) (starter-val))))

(defn move [key-code]
  (let [dir (key-map key-code)]
    (if dir
      (update-board!
       (with-randoms (shift-board dir (@app :board)) )))))

(events/listen js/document "keydown"
               (fn [e] (move (.-keyCode e))))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

