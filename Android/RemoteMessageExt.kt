import android.os.Bundle
import com.google.firebase.messaging.RemoteMessage

/**
  * Extension to copy RemoteMessage with different value
*/
fun RemoteMessage.copy(
  senderId: String = this.senderId ?: "",
  from: String = this.from ?: "",
  to: String = this.to ?: "",
  rawData: ByteArray = this.rawData ?: ByteArray(0),
  collapseKey: String = this.collapseKey ?: "",
  messageId: String = this.messageId ?: "",
  messageType: String = this.messageType ?: "",
  sentTime: Long = this.sentTime,
  ttl: Int = this.ttl,
  originalPriority: Int = this.originalPriority,
  priority: Int = this.priority
): RemoteMessage {
  return RemoteMessage(Bundle().apply {
    putString("google.c.sender.id", senderId)
    putString("from", from)
    putString("google.to", to)
    putByteArray("rawData", rawData)
    putString("collapse_key", collapseKey)
    putString("google.message_id", messageId)
    putString("message_type", messageType)
    putLong("google.sent_time", sentTime)
    putInt("google.ttl", ttl)
    putInt("google.original_priority", originalPriority)
    putInt("google.delivered_priority", priority)
  })
}
