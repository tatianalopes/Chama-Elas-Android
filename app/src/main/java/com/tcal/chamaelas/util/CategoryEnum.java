package com.tcal.chamaelas.util;

import com.tcal.chamaelas.R;

public enum CategoryEnum {
    ELECTRICAL(R.string.category_electrical, R.color.category_electrical, R.drawable.ic_category_electrical),
    PAINTING(R.string.category_painting, R.color.category_painting, R.drawable.ic_category_painting),
    HYDRAULICS(R.string.category_hydraulics, R.color.category_hydraulics, R.drawable.ic_category_hydraulics),
    CONSTRUCTION(R.string.category_construction, R.color.category_construction, R.drawable.ic_category_construction),
    GENERAL_SERVICES(R.string.category_general_services, R.color.category_general_services, R.drawable.ic_category_general_services),
    OTHERS(R.string.category_others, R.color.category_others, R.drawable.ic_category_others);

    private int mStringId;
    private int mColorId;
    private int mImageId;

    CategoryEnum(int stringId, int colorId, int imageId) {
        mStringId = stringId;
        mColorId = colorId;
        mImageId = imageId;
    }

    public int getStringId() {
        return mStringId;
    }

    public int getColorId() {
        return mColorId;
    }

    public int getImageId() {
        return mImageId;
    }
}
