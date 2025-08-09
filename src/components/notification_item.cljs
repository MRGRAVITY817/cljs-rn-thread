(ns components.notification-item
  (:require [react-native :as rn]
            [uix.core :refer [$ defui] :as uix]
            [clojure.string :as str]))

(defn format-timestamp
  "Format notification timestamp"
  [timestamp]
  timestamp) ; For now, just return as-is

(defn get-notification-icon
  "Get icon emoji for notification type"
  [type]
  (case type
    :like "❤️"
    :reply "💬"
    :mention "@"
    :follow "👤"
    :repost "🔄"
    "📌"))

(defn get-notification-color
  "Get color for notification type"
  [type]
  (case type
    :like "#f91880"
    :reply "#1d9bf0"
    :mention "#1d9bf0"
    :follow "#1d9bf0"
    :repost "#00ba7c"
    "#536471"))

(defui notification-item
  "Individual notification item"
  [{:keys [notification on-press on-profile-click]}]
  (let [{:keys [id type user content post-content timestamp read? username thread-id]} notification

        get-notification-text (fn [type user content]
                                (case type
                                  :like (str (:name user) " liked your post")
                                  :reply (str (:name user) " replied to your post")
                                  :mention (str (:name user) " mentioned you")
                                  :follow (str (:name user) " started following you")
                                  :repost (str (:name user) " reposted your post")
                                  "New notification"))

        notification-text (get-notification-text type user content)
        icon (get-notification-icon type)
        icon-color (get-notification-color type)]

    ($ rn/TouchableOpacity
       {:style {:flex-direction "row"
                :padding 16
                :background-color (if read? "#ffffff" "#f7f9fa")
                :border-bottom-width 0.5
                :border-bottom-color "#e1e8ed"}
        :on-press #(when on-press (on-press notification))}

       ;; Icon column
       ($ rn/View {:style {:width 40
                           :align-items "center"
                           :margin-right 12}}
          ($ rn/Text {:style {:font-size 20
                              :color icon-color}}
             icon))

       ;; Content column
       ($ rn/View {:style {:flex 1}}
          ;; User info and notification text
          ($ rn/View {:style {:flex-direction "row"
                              :flex-wrap "wrap"
                              :align-items "center"
                              :margin-bottom 4}}

             ;; User avatar (clickable)
             ($ rn/TouchableOpacity {:style {:width 32
                                             :height 32
                                             :border-radius 16
                                             :background-color "#e1e8ed"
                                             :margin-right 8
                                             :align-items "center"
                                             :justify-content "center"}
                                     :on-press #(when on-profile-click
                                                  (on-profile-click (:username user)))}
                ($ rn/Text {:style {:font-size 12}}
                   "👤"))

             ;; Notification text
             ($ rn/Text {:style {:font-size 15
                                 :color "#0f1419"
                                 :flex 1}}
                notification-text))

          ;; Post content preview (if applicable)
          (when (and post-content (not= type :follow))
            ($ rn/Text {:style {:font-size 14
                                :color "#536471"
                                :margin-top 4
                                :margin-left 40
                                :line-height 18}}
               (str "\"" (if (> (count post-content) 100)
                           (str (subs post-content 0 100) "...")
                           post-content) "\"")))

          ;; Timestamp
          ($ rn/Text {:style {:font-size 12
                              :color "#536471"
                              :margin-top 8
                              :margin-left 40}}
             (format-timestamp timestamp)))

       ;; Unread indicator
       (when (not read?)
         ($ rn/View {:style {:width 8
                             :height 8
                             :border-radius 4
                             :background-color "#1d9bf0"
                             :margin-left 8
                             :margin-top 8}})))))