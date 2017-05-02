(ns malodron.salesroom.ui
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [re-frame.core :as rf]
            [semantic-ui.core :as ui]))


(defn top-menu []
  [ui/menu {:inverted true}
   [ui/menu-item {:content "podłoga"}]
   [ui/menu-item {:content "półka"}]])


(defn grid-cell [cords]
  [:div.grid-cell {:on-mouse-down (fn [e]
                                    (case (.-button e)
                                      0 nil
                                      1 (rf/dispatch [:salesroom/toggle-shelf cords])
                                      2 (rf/dispatch [:salesroom/toggle-floor cords])))}])


(defn floor-cell [cords]
  [:div.grid-cell.floor-cell {:on-mouse-down (fn [e]
                                               (case (.-button e)
                                                 0 nil
                                                 1 (rf/dispatch [:salesroom/toggle-shelf cords])
                                                 2 (rf/dispatch [:salesroom/toggle-floor cords])))}])


(defn shelf-cell [cords]
  (let [place (rf/subscribe [:salesroom/segment-place cords])]
    (fn [cords]
      [ui/popup {:trigger  (r/as-element [:div.grid-cell.shelf-cell {:on-mouse-down  (fn [e]
                                                                                       (case (.-button e)
                                                                                         0 nil
                                                                                         1 (rf/dispatch [:salesroom/toggle-shelf cords])
                                                                                         2 (rf/dispatch [:salesroom/toggle-floor cords])))
                                                                     :on-mouse-enter #(rf/dispatch [:salesroom/set-segment-hovered cords])
                                                                     :on-mouse-leave #(rf/dispatch [:salesroom/reset-segment-hovered])}])
                 :inverted true
                 :content  @place}])))


(defn rack-cell []
  [:div.grid-cell.rack-cell])


(defn cell [cords]
  (let [floor (rf/subscribe [:salesroom/floor])
        shelves (rf/subscribe [:salesroom/shelves])]
    (fn [cords]
      (cond
        (contains? @shelves cords)
        [shelf-cell cords]
        (contains? @floor cords)
        [floor-cell cords]
        :default
        [grid-cell cords]))))


(defn component []
  (let [height (rf/subscribe [:salesroom/height])
        width (rf/subscribe [:salesroom/width])
        floor (rf/subscribe [:salesroom/floor])
        shelves (rf/subscribe [:salesroom/shelves])
        hover? (r/atom false)]
    (fn []
      [ui/grid {:style {:align-items   :flex-start
                        :align-content :flex-start}}
       [ui/grid-row {:columns :equal}
        [ui/grid-column]
        [ui/grid-column {:width 12}
         [top-menu]]
        [ui/grid-column]]
       [ui/grid-row
        [ui/grid-column {:width          10
                         :stretched      true
                         :on-mouse-enter #(reset! hover? true)
                         :on-mouse-leave #(reset! hover? false)}
         [ui/grid
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
                 [:div.row.center-xs
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
                                :opacity    (if @hover? 1.0 0.0)}}]]]]
        [ui/grid-column {:width 6}
         [:div "menu"]]]])))