package com.lepanda.studioneopanda.go4lunch.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lepanda.studioneopanda.go4lunch.models.Workmate;

public class WorkerHelper {

    private static final String COLLECTION_NAME = "workmates";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String uid, String username, String urlPicture) {
        Workmate userToCreate = new Workmate(uid, username, urlPicture);
        return WorkerHelper.getUsersCollection()
                .document(uid)
                .set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid){
        return WorkerHelper.getUsersCollection().document(uid).get();
    }
}
