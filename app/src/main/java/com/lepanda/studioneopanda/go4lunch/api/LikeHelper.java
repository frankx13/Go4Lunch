package com.lepanda.studioneopanda.go4lunch.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lepanda.studioneopanda.go4lunch.models.Like;

public class LikeHelper {

    private static final String COLLECTION_NAME = "likes";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getLikesCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createLikes(String RId, String userLiker) {
        Like likeToCreate = new Like(RId, userLiker);
        return WorkerHelper.getUsersCollection()
                .document(RId)
                .set(likeToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getLikes(String RId) {
        return LikeHelper.getLikesCollection().document(RId).get();
    }
}
