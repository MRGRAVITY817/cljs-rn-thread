(ns components.feed-header
  (:require [react-native :as rn]
            [uix.core :refer [$ defui] :as uix]))

(defui feed-header [{:keys [on-search-click on-notifications-click on-messages-click unread-count]}]
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

        ;; Action buttons
        ($ rn/View {:style {:flex-direction "row"
                            :align-items "center"
                            :gap 12}}

           ;; Notifications button with badge
           ($ rn/View {:style {:position "relative"}}
              ($ rn/TouchableOpacity {:on-press on-notifications-click
                                      :style {:padding 8
                                              :border-radius 20
                                              :background-color "#f7f9fa"}}
                 ($ rn/Text {:style {:font-size 16}} "🔔"))

              ;; Badge for unread notifications
              (when (and unread-count (> unread-count 0))
                ($ rn/View {:style {:position "absolute"
                                    :top 4
                                    :right 4
                                    :background-color "#1d9bf0"
                                    :border-radius 10
                                    :min-width 20
                                    :height 20
                                    :justify-content "center"
                                    :align-items "center"
                                    :border-width 2
                                    :border-color "white"}}
                   ($ rn/Text {:style {:color "white"
                                       :font-size 12
                                       :font-weight "bold"}}
                      (str (min unread-count 99))))))

           ;; Search button
           ;; Messages button
           ($ rn/TouchableOpacity {:on-press on-messages-click
                                   :style {:padding 8
                                           :border-radius 20
                                           :background-color "#f7f9fa"}}
              ($ rn/Text {:style {:font-size 16}} "💬"))

           ;; Search button
           ($ rn/TouchableOpacity {:on-press on-search-click
                                   :style {:padding 8
                                           :border-radius 20
                                           :background-color "#f7f9fa"}}
              ($ rn/Text {:style {:font-size 16}} "🔍"))))))
