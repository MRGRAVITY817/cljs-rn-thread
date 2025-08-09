(ns components.feed-header
  (:require [react-native :as rn]
            [uix.core :refer [$ defui] :as uix]))

(defui feed-header []
  ($ rn/View {:style {:background-color "white"
                      :border-bottom-width 0.5
                      :border-bottom-color "#e1e8ed"
                      :padding-vertical 16
                      :padding-horizontal 16}}
     ($ rn/Text {:style {:font-size 20
                         :font-weight "bold"
                         :color "#0f1419"}}
        "Home")))
