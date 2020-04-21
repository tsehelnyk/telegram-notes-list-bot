import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

public class FirebaseService {

    public ItemsList saveItemsList(ItemsList itemsList) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("notes")
                .document(itemsList.getUser())
                .set(itemsList);
        return itemsList;
    }

    public ItemsList getItemsList(String name) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("notes").document(name);
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot document = future.get();

        ItemsList itemsList = null;

        if(document.exists()) {
            itemsList = document.toObject(ItemsList.class);
        }
        return itemsList;
    }

    public ItemsList updateItemsList(ItemsList itemsList) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("notes")
                .document(itemsList.getUser())
                .set(itemsList);
        return itemsList;
//        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public String deleteItemsList(String name) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = dbFirestore.collection("notes").document(name).delete();
        return "Document with ID " + name + " has been deleted";
    }
}
