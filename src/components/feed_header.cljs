(ns components.feed-header
  (:require [react-native :as rn]
            [uix.core :refer [$ defui] :as uix]))

(defui feed-header [{:keys [on-search-click]}]
  ($ rn/SafeAreaView {:style {:background-color "white"
                              :border-bottom-width 0.5
                              :border-bottom-color "#e1e8ed"}}
     ($ rn/View {:style {:flex-direction "row"
                         :align-items "center"
                         :justify-content "space-between"
                         :padding-horizontal 16
                         :padding-vertical 12}}

        ;; App title/logo
        ($ rn/Text {:style {:font-size 20
                            :font-weight "bold"
                            :color "#0f1419"}}
           "Home")

        ;; Search button
        ($ rn/TouchableOpacity {:on-press on-search-click
                                :style {:padding 8
                                        :border-radius 20
                                        :background-color "#f7f9fa"}}
           ($ rn/Text {:style {:font-size 16}} "🔍")))))
