package com.lepanda.studioneopanda.go4lunch.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lepanda.studioneopanda.go4lunch.models.Workmate;

public class WorkerHelper {

    private static final String COLLECTION_NAME = "workmates";

    // --- COLLECTION REFERENCE ---

    private static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String uid, String username, String urlPicture) {
        Workmate userToCreate = new Workmate();
        return WorkerHelper.getUsersCollection()
                .document(uid)
                .set(userToCreate);
    }
}
