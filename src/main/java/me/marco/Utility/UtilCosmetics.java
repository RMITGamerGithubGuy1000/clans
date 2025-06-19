package me.marco.Utility;

public class UtilCosmetics {

//    @SuppressWarnings("unchecked")
//    public static void setGlowing(Player glowingPlayer, Player sendPacketPlayer, boolean glow) {
//        EntityPlayer entityPlayer = ((CraftPlayer) glowingPlayer).getHandle();
//        entityPlayer.glowing = true;
//
//        DataWatcher dataWatcher = entityPlayer.getDataWatcher();
//
//        entityPlayer.glowing = glow; // For the update method in EntityPlayer to prevent switching back.
////
////            // The map that stores the DataWatcherItems is private within the DataWatcher Object.
////            // We need to use Reflection to access it from Apache Commons and change it.
////            Map<Integer, DataWatcher.Item<?>> map = (Map<Integer, DataWatcher.Item<?>>) FieldUtils.readDeclaredField(dataWatcher, "d", true);
////
////            // Get the 0th index for the BitMask value. http://wiki.vg/Entities#Entity
////            DataWatcher.Item item = map.get(0);
////            byte initialBitMask = (Byte) item.b(); // Gets the initial bitmask/byte value so we don't overwrite anything.
////            byte bitMaskIndex = (byte) 0x40; // The index as specified in wiki.vg/Entities
////            if (glow) {
////                item.a((byte) (initialBitMask | 1 << bitMaskIndex));
////            } else {
////                item.a((byte) (initialBitMask & ~(1 << bitMaskIndex))); // Inverts the specified bit from the index.
////            }
//
//        PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(glowingPlayer.getEntityId(), dataWatcher, true);
//
//        ((CraftPlayer) sendPacketPlayer).getHandle().playerConnection.sendPacket(metadataPacket);
//
//        for(Player o : Bukkit.getOnlinePlayers()){
//            if(o == sendPacketPlayer) return;
//            PacketPlayOutEntityMetadata metadataPacket2 = new PacketPlayOutEntityMetadata(glowingPlayer.getEntityId(), dataWatcher, true);
//            ((CraftPlayer) o).getHandle().playerConnection.sendPacket(metadataPacket);
//        }
//
//    }

}
