package com.sparklead.newsnow.service

import com.sparklead.newsnow.model.PushNotification

interface NotificationService {

    suspend fun pushNotification(notification: PushNotification)

}