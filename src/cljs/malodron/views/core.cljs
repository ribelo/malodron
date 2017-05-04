(ns malodron.views.core
  (:require [re-frame.core :as rf]
            [semantic-ui.core :as ui]
            [malodron.salesroom.ui :as salesroom]
            [malodron.flex :as flex]
            ))

;(defn main-panel []
;  (fn []
;    [ui/grid {:columns   1
;              :padded    true
;              :container true
;              :style     {:min-width "100%"}}
;     [ui/grid-column
;      [salesroom/component]]]))

(defn main-panel []
  (fn []
    [flex/vbox {:size "1"}
     [flex/hbox {:size     "10%"
                 :on-click #(println "sex!!")
                 :style    {:background-color :gold
                            :justify-content  :center
                            :align-items      :center}}
      [:div {:style {:background-color "blue"}}
       "sex"]]
     [flex/hbox {:size     "10%"
                 :on-click #(println "sex!!")
                 :style    {:background-color :red
                            :align-content    :stretch}}
      [:div "sex"]]]))