(ns malodron.salesroom.ui
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [re-frame.core :as rf]
            [taoensso.encore :refer [reset-in!]]
            [semantic-ui.core :as ui]))


(defn top-menu []
  [ui/menu {:inverted true}
   [ui/menu-item {:content "podłoga"}]
   [ui/menu-item {:content "półka"}]])


(defn grid-cell [cords]
  [:div.grid-cell {:on-mouse-down (fn [e]
                                    (case (.-button e)
                                      0 nil
                                      1 (rf/dispatch [:salesroom/toggle-segment cords])
                                      2 (rf/dispatch [:salesroom/toggle-floor cords])))}])


(defn floor-cell [cords]
  [:div.grid-cell.floor-cell {:on-mouse-down (fn [e]
                                               (case (.-button e)
                                                 0 nil
                                                 1 (rf/dispatch [:salesroom/toggle-segment cords])
                                                 2 (rf/dispatch [:salesroom/toggle-floor cords])))}])


(defn segment-cell [cords]
  (let [place (rf/subscribe [:salesroom/segment-place cords])]
    (fn [cords]
      [ui/popup {:trigger  (r/as-element [:div.grid-cell.shelf-cell {:on-mouse-down  (fn [e]
                                                                                       (case (.-button e)
                                                                                         0 (rf/dispatch [:salesroom/select-segment cords])
                                                                                         1 (rf/dispatch [:salesroom/toggle-segment cords])
                                                                                         2 (rf/dispatch [:salesroom/toggle-floor cords])))
                                                                     :on-mouse-enter #(rf/dispatch [:salesroom/set-segment-hovered cords])
                                                                     :on-mouse-leave #(rf/dispatch [:salesroom/reset-segment-hovered])}])
                 :inverted true
                 :content  @place}])))

(defn product-cell []
  )


(defn cell [cords]
  (let [floor (rf/subscribe [:salesroom/floor])
        segments (rf/subscribe [:salesroom/segments])]
    (fn [cords]
      (cond
        (contains? @segments cords)
        [segment-cell cords]
        (contains? @floor cords)
        [floor-cell cords]
        :default
        [grid-cell cords]))))


(defn segment []
  (let [shelves (rf/subscribe [:salesroom/selected-segment-shelves])
        selected-segment (rf/subscribe [:salesroom/selected-segment])
        place (rf/subscribe [:salesroom/selected-segment-place])
        hover? (r/atom false)
        shelf-hover? (r/atom {})]
    (fn []
      [ui/grid {:on-mouse-enter #(reset! hover? true)
                :on-mouse-leave #(reset! hover? false)}
       [ui/grid-row {:centered true}
        [:div (str "segment " @place)]]
       [ui/grid-row {:centered true}
        [ui/icon {:name     "minus"
                  :on-click #(rf/dispatch [:salesroom/remove-shelf-from-segment])
                  :style    {:transition "all 450ms cubic-bezier(0.23, 1, 0.32, 1) 0ms"
                             :opacity    (if @hover? 1.0 0.0)}}]]
       [ui/grid-row {:centered true}
        [ui/grid {:centered true
                  :padded   true}
         (doall
           (for [[shelf-idx shelf] (map vector (range) @shelves)]
             [ui/grid-row {:centered       true
                           :vertical-align :middle
                           :on-mouse-enter #(reset-in! shelf-hover? [shelf-idx] true)
                           :on-mouse-leave #(reset-in! shelf-hover? [shelf-idx] false)
                           :style          {:margin      "0px"
                                            :padding     "0px"
                                            :align-items :center}}

              [ui/icon {:name     "minus"
                        :on-click #(rf/dispatch [:salesroom/remove-product-from-shelf shelf-idx])
                        :style    {:transition "all 450ms cubic-bezier(0.23, 1, 0.32, 1) 0ms"
                                   :opacity    (if (get @shelf-hover? shelf-idx) 1.0 0.0)}}]
              [:div {:style {:display   :flex
                             :flex-flow "row nowrap"
                             :flex      "1 0 0"}}
               (doall (for [[product-idx {:keys [product/width]
                                          :or   {width 1}
                                          :as   product}] (map vector (range) shelf)]
                        [:div.product-cell {:on-click #(println :bla @selected-segment shelf-idx product-idx)
                                            :style    {:display     :flex
                                                       :flex-flow   "row nowrap"
                                                       :flex-grow   1
                                                       :flex-shrink 0
                                                       :flex-basis  0}}
                         (doall
                           (for [_ (range width)]
                             [ui/image {:src      "img/products/8006040710512.jpg"
                                        :height   "25px"
                                        :centered true}]))]))]
              [ui/icon {:name     "plus"
                        :on-click #(rf/dispatch [:salesroom/add-product-to-shelf shelf-idx])
                        :style    {:transition "all 450ms cubic-bezier(0.23, 1, 0.32, 1) 0ms"
                                   :opacity    (if (get @shelf-hover? shelf-idx) 1.0 0.0)}}]]))]]
       [ui/grid-row {:centered true}
        [ui/icon {:name     "plus"
                  :on-click #(rf/dispatch [:salesroom/add-shelf-to-segment])
                  :style    {:transition "all 450ms cubic-bezier(0.23, 1, 0.32, 1) 0ms"
                             :opacity    (if @hover? 1.0 0.0)}}]]])))


;(defn product-component []
;  (let [product-idx (rf/subscribe [:salesroom/shelves])
;        product (reaction (get-in @shelves [segment-idx shelf-idx product-idx]))]
;    (fn [segment-idx shelf-idx product-idx]
;      [ui/grid
;       [ui/grid-row
;        [:div (str segment-idx shelf-idx product-idx)]]])))



(defn salesroom []
  (let [height (rf/subscribe [:salesroom/height])
        width (rf/subscribe [:salesroom/width])
        hover? (r/atom false)]
    (fn []
      [ui/grid {:on-mouse-enter #(reset! hover? true)
                :on-mouse-leave #(reset! hover? false)}
       [ui/grid-row {:centered true}
        [ui/icon {:name     "minus"
                  :on-click #(rf/dispatch [:salesroom/set-height (dec @height)])
                  :style    {:transition "all 450ms cubic-bezier(0.23, 1, 0.32, 1) 0ms"
                             :opacity    (if @hover? 1.0 0.0)}}]]
       [ui/grid-row {:centered true}
        [ui/grid-column {:vertical-align :middle}
         [ui/icon {:name     "minus"
                   :on-click #(rf/dispatch [:salesroom/set-width (dec @width)])
                   :style    {:transition "all 450ms cubic-bezier(0.23, 1, 0.32, 1) 0ms"
                              :opacity    (if @hover? 1.0 0.0)}}]]
        [ui/grid-column {:width @width}
         [:div
          (doall
            (for [x (range @height)]
              ^{:key x}
              [:div {:style {:display :flex
                             :flex-flow "row nowrap"
                             :justify-content :center}}
               (doall
                 (for [y (range @width)]
                   ^{:key y}
                   [cell [x y]]))]))]]
        [ui/grid-column {:vertical-align :middle}
         [ui/icon {:name     "plus"
                   :on-click #(rf/dispatch [:salesroom/set-width (inc @width)])
                   :style    {:transition "all 450ms cubic-bezier(0.23, 1, 0.32, 1) 0ms"
                              :opacity    (if @hover? 1.0 0.0)}}]]]
       [ui/grid-row {:centered true}
        [ui/icon {:name     "plus"
                  :on-click #(rf/dispatch [:salesroom/set-height (inc @height)])
                  :style    {:transition "all 450ms cubic-bezier(0.23, 1, 0.32, 1) 0ms"
                             :opacity    (if @hover? 1.0 0.0)}}]]])))


(defn component []
  (let []
    (fn []
      [:div {}]

      [ui/grid {:style {:align-items   :flex-start
                        :align-content :flex-start}}
       [ui/grid-row {:columns :equal}
        [ui/grid-column]
        [ui/grid-column {:width 12}
         [top-menu]]
        [ui/grid-column]]
       [ui/grid-row
        [ui/grid-column {:width     10
                         :stretched true}
         [salesroom]]
        [ui/grid-column {:width     6
                         :stretched true}
         [:div "sex"]]]])))