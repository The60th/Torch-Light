package com.worldciv.utility;

import com.google.common.collect.*;
import it.unimi.dsi.fastutil.Hash;

import java.util.*;

public final class utilityMultimaps {

    public final static Multimap<String, String> partyrequest = HashMultimap.create();

    public static Multimap<String, String> partyid = HashMultimap.create(); // sender , uuidtostring

    public static Multimap<String, String> partyleaderid = HashMultimap.create(); // sender , uuidtostring

    public static Multimap<String, String> blockedplayers = HashMultimap.create(); //sender , blockedplayer


}
