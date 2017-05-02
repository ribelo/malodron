(ns malodron.views.core
  (:require [re-frame.core :as rf]
            [semantic-ui.core :as ui]
            [malodron.salesroom.ui :as salesroom]))

(defn main-panel []
  (fn []
    [:div {:style {:height "100%"}}
     [ui/menu {:inverted true
               :vertical true
               :fixed    :left
               :style    {:width "250px"}}]
     [:div {:style {:height      "100%"
                    :margin-left "250px"}}
      [ui/grid {:columns   1
                :padded    true
                :container true
                :style {:min-width "100%"}}
       [ui/grid-column
        [salesroom/component]]]]]))