package com.flatlyapps.materialbudget.card;


import com.flatlyapps.materialbudget.R;
import com.flatlyapps.materialbudget.card.activity.AbstractActivity;
import com.flatlyapps.materialbudget.card.activity.DocumentActivity;
import com.flatlyapps.materialbudget.card.cardcontent.AbstractContentView;
import com.flatlyapps.materialbudget.card.cardcontent.DocumentContentView;

/**
 * Created by Paul on 21/07/2015.
 */
public class CardCategorySelector {

    private CardCategorySelector(){}

    public enum CardCategory {

        DOCUMENTS1(R.string.card_title_documents, DocumentActivity.class, DocumentContentView.class),
        DOCUMENTS2(R.string.card_title_documents, DocumentActivity.class, DocumentContentView.class),
        DOCUMENTS3(R.string.card_title_documents, DocumentActivity.class, DocumentContentView.class),
        DOCUMENTS4(R.string.card_title_documents, DocumentActivity.class, DocumentContentView.class),
        DOCUMENTS5(R.string.card_title_documents, DocumentActivity.class, DocumentContentView.class),
        DOCUMENTS6(R.string.card_title_documents, DocumentActivity.class, DocumentContentView.class);

        private final int title;
        private final Class<? extends AbstractActivity> activity;
        private final Class<? extends AbstractContentView> contentView;

        CardCategory(int title, Class<? extends AbstractActivity> activity, Class<? extends AbstractContentView> contentView) {
            this.title = title;
            this.activity = activity;
            this.contentView = contentView;
        }

        public int getTitle() {
            return title;
        }

        public Class<? extends AbstractActivity> getActivity() {
            return activity;
        }

        public Class<? extends AbstractContentView> getContentView() {
            return contentView;
        }
    }
}
