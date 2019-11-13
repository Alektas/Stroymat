package alektas.stroymat.data.db;

import android.util.Log;

import androidx.annotation.VisibleForTesting;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alektas.stroymat.BuildConfig;
import alektas.stroymat.R;
import alektas.stroymat.data.ItemsRepository;
import alektas.stroymat.data.Repository;
import alektas.stroymat.data.db.entities.Bordur;
import alektas.stroymat.data.db.entities.Category;
import alektas.stroymat.data.db.entities.Plita;
import alektas.stroymat.data.db.entities.PricelistItem;
import alektas.stroymat.data.db.entities.Profnastil;
import alektas.stroymat.data.db.entities.Siding;
import alektas.stroymat.data.db.entities.Size;
import alektas.stroymat.data.db.entities.StoveBrick;
import alektas.stroymat.data.db.entities.Photo;
import alektas.stroymat.utils.ResourcesUtils;

public class FirestoreLoader {
    private static final String TAG = "PricelistMigration";
    private FirebaseFirestore firebase;
    private Repository repository;

    @VisibleForTesting
    public FirestoreLoader() {
    }

    public FirestoreLoader(FirebaseFirestore firebase, Repository repository) {
        this.firebase = firebase;
        this.repository = repository;
        // Методы загрузки в Firestore можно писать сюда
    }

    public void loadPricelist(ItemsRepository.LoadingListener listener) {
        if (BuildConfig.DEBUG) Log.d(TAG, "loadPricelist: start loading");

        firebase.collection("pricelist")
                .get()
                .addOnSuccessListener(task -> {
                    if (BuildConfig.DEBUG) Log.d(TAG, "loadPricelist: success");

                    List<PricelistItem> pricelist = new ArrayList<>();
                    List<Size> sizes = new ArrayList<>();
                    List<Siding> sidings = new ArrayList<>();
                    List<Profnastil> profnastil = new ArrayList<>();
                    List<StoveBrick> bricks = new ArrayList<>();
                    List<Plita> plitas = new ArrayList<>();
                    List<Bordur> bordurs = new ArrayList<>();
                    for (DocumentSnapshot document : task.getDocuments()) {
                        Map<String, Object> doc = document.getData();
                        if (doc == null) {
                            Log.d(TAG, "loadPricelist: pricelist item is null");
                            continue;
                        }

                        try {
                            int article = intFrom(doc.get("article"));
                            String name = (String) doc.get("name");
                            float price = floatFrom(doc.get("price"));
                            String unit = (String) doc.get("unit");
                            if (unit == null) unit = "";
                            int categ = intFrom(doc.get("categ"));
                            String url = (String) doc.get("url");

                            if (article == 0 || name == null) continue;
                            PricelistItem item = new PricelistItem(article, name, price, unit, url, categ);
                            pricelist.add(item);

                            float length = floatFrom(doc.get("length"));
                            float width = floatFrom(doc.get("width"));
                            if (length != 0f && width != 0f) {
                                Size size = new Size(article, length, width);
                                sizes.add(size);
                            }

                            int isProfnastil = intFrom(doc.get("proflist"));
                            if (isProfnastil == 1) {
                                float overlap = floatFrom(doc.get("overlap"));
                                Profnastil proflist = new Profnastil(article, overlap);
                                profnastil.add(proflist);
                            }

                            int isSiding = intFrom(doc.get("siding"));
                            if (isSiding == 1) {
                                Siding siding = new Siding(article);
                                sidings.add(siding);
                            }

                            int isStove = intFrom(doc.get("stove"));
                            if (isStove == 1) {
                                StoveBrick brick = new StoveBrick(article);
                                bricks.add(brick);
                            }

                            int isPlita = intFrom(doc.get("plate"));
                            if (isPlita == 1) {
                                Plita plita = new Plita(article);
                                plitas.add(plita);
                            }

                            int isBorder = intFrom(doc.get("border"));
                            if (isBorder == 1) {
                                Bordur border = new Bordur(article);
                                bordurs.add(border);
                            }

                        } catch (NumberFormatException | ClassCastException e) {
                            Log.e(TAG, "loadPricelist: format of the item field is not valid!", e);
                        }
                    }

                    if (pricelist.isEmpty()) return;

                    repository.setPricelist(pricelist);
                    repository.setSizes(sizes);
                    repository.setSiding(sidings);
                    repository.setProfnastil(profnastil);
                    repository.setStoveBricks(bricks);
                    repository.setPlity(plitas);
                    repository.setBordury(bordurs);

                    listener.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failure on loading price list", e);
                });
    }

    public void loadCategories(ItemsRepository.LoadingListener listener) {
        if (BuildConfig.DEBUG) Log.d(TAG, "loadCategories: start loading");

        firebase.collection("categories")
                .get()
                .addOnSuccessListener(task -> {
                    if (BuildConfig.DEBUG) Log.d(TAG, "loadCategories: success");

                    List<Category> categories = new ArrayList<>();
                    for (DocumentSnapshot document : task.getDocuments()) {
                        Map<String, Object> doc = document.getData();
                        if (doc == null) {
                            Log.d(TAG, "loadCategories: category is null");
                            continue;
                        }

                        try {
                            int categ = intFrom(doc.get("categ"));
                            if ((categ) == 0) continue;
                            String categName = (String) doc.get("name");
                            String categImg = (String) doc.get("url");
                            Category category = new Category(categ, categName == null ?
                                    ResourcesUtils.getString(R.string.other) : categName, categImg);
                            categories.add(category);
                        } catch (NumberFormatException | ClassCastException e) {
                            Log.e(TAG, "updateCategories: categ should be an integer and name a text!", e);
                        }
                    }

                    if (categories.isEmpty()) return;

                    repository.setCategories(categories);

                    listener.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failure on loading categories", e);
                });
    }

    public void loadGallery() {
        if (BuildConfig.DEBUG) Log.d(TAG, "loadGallery: start loading");

        firebase.collection("gallery")
                .get()
                .addOnSuccessListener(task -> {
                    if (BuildConfig.DEBUG) Log.d(TAG, "loadGallery: success");

                    List<Photo> photos = new ArrayList<>();
                    for (DocumentSnapshot document : task.getDocuments()) {
                        Map<String, Object> doc = document.getData();
                        if (doc == null) {
                            Log.d(TAG, "loadGallery: gallery photo is null");
                            continue;
                        }

                        try {
                            String uri = (String) doc.get("url");
                            if (uri == null) break;
                            photos.add(new Photo(uri, document.getId()));
                        } catch (NumberFormatException | ClassCastException e) {
                            Log.e(TAG, "loadGallery: uri should be in a text format!", e);
                        }
                    }

                    repository.setGalleryPhotos(photos);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failure on loading gallery", e);
                });
    }

    public void deleteEmptyDocuments() {
        firebase.collection("pricelist").whereEqualTo("article", null).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    firebase.collection("pricelist").document(document.getId()).delete();
                }
            }
        });
    }

    public void resetPricelist() {
        firebase.collection("pricelist").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    firebase.collection("pricelist").document(document.getId()).delete();
                }
            }
        });
    }

    @VisibleForTesting
    public float floatFrom(Object object) {
        float f = 0f;

        if (object == null) return f;
        if (object instanceof Long) {
            f = (float) ((long) object);
        } else if (object instanceof Double) {
            f = (float) ((double) object);
        } else if (object instanceof String) {
            try {
                f = Float.parseFloat(object.toString());
            } catch (NumberFormatException e) {
                Log.d(TAG, "floatFrom: " + object + ". Field is not a float!");
            }
        }

        return f;
    }


    @VisibleForTesting
    public int intFrom(Object object) {
        int f = 0;

        if (object == null) return f;
        if (object instanceof Long) {
            f = (int) ((long) object);
        } else if (object instanceof Double) {
            f = (int) ((double) object);
        } else if (object instanceof String) {
            try {
                f = Integer.parseInt(object.toString());
            } catch (NumberFormatException e) {
                Log.d(TAG, "integerFrom: " + object + ". Field is not an integer!");
            }
        }

        return f;
    }

    /**
     * Перед загрузкой необходимо стереть старые категории
     */
    private void loadCategoriesToFirebase() {
        List<Category> categories = repository.getCategories().getValue();
        if (categories == null) return;
        List<Map<String, Object>> docs = new ArrayList<>();
        for (Category category : categories) {
            Map<String, Object> doc = new HashMap<>();
            doc.put("url", category.getCategImg());
            doc.put("name", category.getCategName());
            doc.put("categ", category.getCateg());
            docs.add(doc);
        }
        for (Map<String, Object> doc : docs) {
            firebase.collection("categories").add(doc);
        }
    }

    /**
     * Перед загрузкой необходимо стереть старый прайслист с помощью
     * {@link FirestoreLoader#resetPricelist()}
     */
    private void loadPricelistToFirebase() {
        List<PricelistItem> list = repository.getItems(0);
        List<Size> sizes = repository.getSizesList();
        List<Profnastil> profnastils = repository.getProfnastilList();
        List<Siding> siding = repository.getSidingList();
        List<StoveBrick> bricks = repository.getStoveBricksList();
        List<Plita> plity = repository.getPlityList();
        List<Bordur> bordury = repository.getBorduryList();

        List<Map<String, Object>> pricelist = new ArrayList<>();

        for (PricelistItem item : list) {
            Map<String, Object> doc = new HashMap<>();
            doc.put("article", item.getArticle());
            doc.put("name", item.getName());
            doc.put("price", item.getPrice());
            doc.put("unit", item.getUnit());
            doc.put("url", item.getImgResName());
            doc.put("categ", item.getCateg());

            Size size = findSizes(item.getArticle(), sizes);
            if (size != null) {
                doc.put("length", size.getLength());
                doc.put("width", size.getWidth());
            }

            Profnastil profnastil = findProfnastil(item.getArticle(), profnastils);
            if (profnastil != null) {
                doc.put("proflist", 1);
                doc.put("overlap", profnastil.getOverlap());
            }

            Siding sidingPiece = findSiding(item.getArticle(), siding);
            if (sidingPiece != null) {
                doc.put("siding", 1);
            }

            StoveBrick brick = findBrick(item.getArticle(), bricks);
            if (brick != null) {
                doc.put("stove", 1);
            }

            Plita plita = findPlita(item.getArticle(), plity);
            if (plita != null) {
                doc.put("plate", 1);
            }

            Bordur bordur = findBordur(item.getArticle(), bordury);
            if (bordur != null) {
                doc.put("border", 1);
            }

            pricelist.add(doc);
        }

        for (Map<String, Object> doc : pricelist) {
            firebase.collection("pricelist").add(doc);
        }
    }

    private Size findSizes(int article, List<Size> sizes) {
        for (Size item : sizes) {
            if (item.getItemArticle() == article) return item;
        }
        return null;
    }

    private Profnastil findProfnastil(int article, List<Profnastil> profnastils) {
        for (Profnastil item : profnastils) {
            if (item.getItemArticle() == article) return item;
        }
        return null;
    }

    private Siding findSiding(int article, List<Siding> siding) {
        for (Siding item : siding) {
            if (item.getItemArticle() == article) return item;
        }
        return null;
    }

    private StoveBrick findBrick(int article, List<StoveBrick> bricks) {
        for (StoveBrick item : bricks) {
            if (item.getItemArticle() == article) return item;
        }
        return null;
    }

    private Plita findPlita(int article, List<Plita> plitas) {
        for (Plita item : plitas) {
            if (item.getItemArticle() == article) return item;
        }
        return null;
    }

    private Bordur findBordur(int article, List<Bordur> bordurs) {
        for (Bordur item : bordurs) {
            if (item.getItemArticle() == article) return item;
        }
        return null;
    }

}
