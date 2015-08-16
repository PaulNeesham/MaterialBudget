package com.flatlyapps.materialBudget.card;


import com.flatlyapps.materialBudget.R;
import com.flatlyapps.materialBudget.card.activity.AbstractActivity;
import com.flatlyapps.materialBudget.card.activity.DocumentActivity;
import com.flatlyapps.materialBudget.card.cardcontent.AbstractContentView;
import com.flatlyapps.materialBudget.card.cardcontent.DocumentContentView;

/**
 * Created by Paul on 21/07/2015.
 */
public enum SummaryCardCategory {


        DOCUMENTS1(R.string.card_title_documents, DocumentActivity.class, DocumentContentView.class),
        DOCUMENTS2(R.string.card_title_documents, DocumentActivity.class, DocumentContentView.class),
        DOCUMENTS3(R.string.card_title_documents, DocumentActivity.class, DocumentContentView.class),
        DOCUMENTS4(R.string.card_title_documents, DocumentActivity.class, DocumentContentView.class),
        DOCUMENTS5(R.string.card_title_documents, DocumentActivity.class, DocumentContentView.class),
        DOCUMENTS6(R.string.card_title_documents, DocumentActivity.class, DocumentContentView.class);

        private final int title;
        private final Class<? extends AbstractActivity> activity;
        private final Class<? extends AbstractContentView> contentView;

        SummaryCardCategory(int title, Class<? extends AbstractActivity> activity, Class<? extends AbstractContentView> contentView) {
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
