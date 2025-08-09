(ns components.feed-item
  (:require
   [react-native :as rn]
   [uix.core :refer [$ defui]]))

(defn format-number [n]
  (cond
    (>= n 1000000) (str (Math/floor (/ n 100000) / 10) "M")
    (>= n 1000) (str (Math/floor (/ n 100) / 10) "K")
    :else (str n)))

(defui action-button [{:keys [icon count active? on-press]}]
  ($ rn/TouchableOpacity {:on-press on-press
                          :style {:flex-direction "row"
                                  :align-items "center"
                                  :padding 8}}
     ($ rn/Text {:style {:font-size 16
                         :margin-right 4
                         :color (if active? "#1d9bf0" "#536471")}}
        icon)
     ($ rn/Text {:style {:font-size 13
                         :color (if active? "#1d9bf0" "#536471")}}
        (format-number count))))

(defui feed-item
  [{:keys [post on-like on-reply on-repost on-profile-click]}]
  ($ rn/View {:style {:background-color "white"
                      :border-bottom-width 0.5
                      :border-bottom-color "#e1e8ed"
                      :padding-horizontal 16
                      :padding-vertical 12}}

           ;; Header with avatar and user info
     ($ rn/View {:style {:flex-direction "row"
                         :align-items "start"
                         :margin-bottom 8}}
        ($ rn/TouchableOpacity {:style {:width 40
                                        :height 40
                                        :border-radius 20
                                        :background-color "#e1e8ed"
                                        :margin-right 12}
                                :on-press #(when on-profile-click
                                             (on-profile-click (get-in post [:user :username])))}
                 ;; Placeholder for avatar
           ($ rn/Text {:style {:text-align "center"
                               :line-height 40
                               :font-size 16
                               :color "#536471"}}
              "👤"))

        ($ rn/View {:style {:flex 1}}
           ($ rn/View {:style {:flex-direction "row"
                               :padding-top 8
                               :align-items "center"}}
              ($ rn/Text {:style {:font-weight "bold"
                                  :font-size 15
                                  :color "#0f1419"}}
                 (get-in post [:user :name]))
              ($ rn/Text {:style {:margin-left 4
                                  :font-size 15
                                  :color "#536471"}}
                 (get-in post [:user :username]))
              ($ rn/Text {:style {:margin-left 4
                                  :font-size 15
                                  :color "#536471"}}
                 "·")
              ($ rn/Text {:style {:margin-left 4
                                  :font-size 15
                                  :color "#536471"}}
                 (:timestamp post)))))

           ;; Post content
     ($ rn/Text {:style {:font-size 15
                         :line-height 20
                         :color "#0f1419"
                         :margin-bottom 12}}
        (:content post))

           ;; Action buttons
     ($ rn/View {:style {:flex-direction "row"
                         :justify-content "space-between"
                         :margin-top 8
                         :max-width 300}}
        ($ action-button {:icon "💬"
                          :count (:replies post)
                          :active? false
                          :on-press #(on-reply (:id post))})
        ($ action-button {:icon "🔄"
                          :count (:reposts post)
                          :active? false
                          :on-press #(on-repost (:id post))})
        ($ action-button {:icon (if (:liked? post) "❤️" "🤍")
                          :count (:likes post)
                          :active? (:liked? post)
                          :on-press #(on-like (:id post))})
        ($ action-button {:icon "📤"
                          :count 0
                          :active? false
                          :on-press #(js/console.log "Share" (:id post))}))))

