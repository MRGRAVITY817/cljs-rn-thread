(ns app.notifications
  (:require [react-native :as rn]
            [uix.core :refer [$ defui] :as uix]
            [app.data :refer [mock-notifications]]
            [components.notification-item :refer [notification-item]]
            [components.flat-list :refer [flat-list]]))

(defui notifications-header [{:keys [on-back]}]
  ($ rn/SafeAreaView {:style {:background-color "white"
                              :border-bottom-width 0.5
                              :border-bottom-color "#e1e8ed"}}
     ($ rn/View {:style {:flex-direction "row"
                         :align-items "center"
                         :padding 16
                         :background-color "#ffffff"
                         :border-bottom-width 0.5
                         :border-bottom-color "#e1e8ed"}}
        ($ rn/TouchableOpacity {:style {:margin-right 16}
                                :on-press on-back}
           ($ rn/Text {:style {:font-size 18
                               :color "#1d9bf0"}}
              "←"))
        ($ rn/Text {:style {:font-size 20
                            :font-weight "bold"
                            :color "#0f1419"}}
           "Notifications"))))

(defui notifications-screen [{:keys [on-back on-profile-click on-thread-click]}]
  "Main notifications screen"
  (let [[notifications set-notifications!] (uix/use-state mock-notifications)
        [unread-count set-unread-count!] (uix/use-state
                                          (count (filter #(not (:read? %)) notifications)))

        handle-mark-as-read (uix/use-callback
                             (fn [notification-id]
                               (set-notifications!
                                (fn [current]
                                  (mapv (fn [notif]
                                          (if (= (:id notif) notification-id)
                                            (assoc notif :read? true)
                                            notif))
                                        current)))
                               (set-unread-count! (fn [count] (max 0 (dec count)))))
                             [])

        handle-mark-all-read (uix/use-callback
                              (fn []
                                (set-notifications!
                                 (fn [current]
                                   (mapv #(assoc % :read? true) current)))
                                (set-unread-count! 0))
                              [])

        handle-notification-press (uix/use-callback
                                   (fn [notification]
                                     (when (not (:read? notification))
                                       (handle-mark-as-read (:id notification)))
                                     (case (:type notification)
                                       :like nil ; Stay on notifications
                                       :reply (when on-thread-click
                                                (on-thread-click (:thread-id notification)))
                                       :mention (when on-thread-click
                                                  (on-thread-click (:thread-id notification)))
                                       :follow (when on-profile-click
                                                 (on-profile-click (:username notification)))
                                       nil))
                                   [on-thread-click on-profile-click handle-mark-as-read])]

    ($ rn/View {:style {:flex 1
                        :background-color "#ffffff"}}

       ;; Header
       ($ rn/SafeAreaView {}
          ($ notifications-header {:on-back on-back}))

       (if (empty? notifications)
         ;; Empty state
         ($ rn/View {:style {:flex 1
                             :justify-content "center"
                             :align-items "center"
                             :padding 32}}
            ($ rn/Text {:style {:font-size 24
                                :margin-bottom 16}}
               "🔔")
            ($ rn/Text {:style {:font-size 18
                                :font-weight "bold"
                                :color "#0f1419"
                                :margin-bottom 8
                                :text-align "center"}}
               "No notifications yet")
            ($ rn/Text {:style {:font-size 14
                                :color "#536471"
                                :text-align "center"
                                :line-height 20}}
               "When someone likes, replies to, or mentions you, you'll see it here."))

         ;; Notifications list
         ($ rn/View {:style {:flex 1}}
            ;; Mark all read button (if there are unread notifications)
            (when (> unread-count 0)
              ($ rn/View {:style {:padding 16
                                  :border-bottom-width 0.5
                                  :border-bottom-color "#e1e8ed"}}
                 ($ rn/TouchableOpacity {:style {:align-self "flex-end"}
                                         :on-press handle-mark-all-read}
                    ($ rn/Text {:style {:color "#1d9bf0"
                                        :font-size 14}}
                       "Mark all as read"))))

            ;; Notifications list
                        ;; Notifications list
            ($ flat-list {:data notifications
                          :key-extractor (fn [item] (str (:id item)))
                          :render-item (fn [item]
                                         ($ notification-item
                                            {:notification item
                                             :on-press handle-notification-press
                                             :on-profile-click on-profile-click}))
                          :style {:flex 1}
                          :shows-vertical-scroll-indicator false}))))))
