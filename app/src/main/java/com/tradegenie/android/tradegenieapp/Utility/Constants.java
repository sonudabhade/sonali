package com.tradegenie.android.tradegenieapp.Utility;


import android.widget.TextView;

import com.tradegenie.android.tradegenieapp.Activity.MainActivity;

public class Constants {
    public static MainActivity mMainActivity;
    public static TextView img_notification_badge;
    public static String OpenedChatId = "";


    //---------------------------------------Session Keys--------------------------------------------------------------------------------------

    public static final String KEY_FCM_ID = "KEY_FCM_ID";
    public static final String IS_NOTIFICATION_RECEIVED = "IS_NOTIFICATION_RECEIVED";
    public static final String IS_NOTIFICATION_RECEIVED_LEADS = "IS_NOTIFICATION_RECEIVED_LEADS";
    public static final String IS_NOTIFICATION_RECEIVED_MESSAGE = "IS_NOTIFICATION_RECEIVED_MESSAGE";
    public static final String KEY_LOGIN_TYPE = "logintype";
    public static final int KEY_TIME_OUT = 50000;
    public final static String KEY_PATH_OF_QUATATION = "/storage/emulated/0/storage/emulated/0/Download";


    public static final String KEY_USER_TYPE = "user_type";
    public static final String USER_LANG = "lang";

    //Seller Details
    public static final String KEY_SELLER_UID = "uid";
    public static final String KEY_SELLER_USER_NAME = "user_Name";
    public static final String KEY_SELLER_EMAIL_ID = "emailid";
    public static final String KEY_SELLER_MOBILE_NO = "mobileno";
    public static final String KEY_SELLER_COUNTRY = "country";
    public static final String KEY_SELLER_COUNTRY_ID = "KEY_SELLER_COUNTRY_ID";
    public static final String KEY_SELLER_CITY = "city";
    public static final String KEY_SELLER_GENDER = "gender";
    public static final String KEY_SELLER_DOB = "dob";
    public static final String KEY_SELLER_ADDRESS = "address";
    public static final String KEY_SELLER_PROFILE = "profile";
    public static final String KEY_SELLER_NATURE_OF_BUSINESS = "KEY_SELLER_NATURE_OF_BUSINESS";


    //buyer Details
    public static final String KEY_BUYER_UID = "buyer_uid";
    public static final String KEY_BUYER_USER_NAME = "buyer_user_Name";
    public static final String KEY_BUYER_EMAIL_ID = "buyer_emailid";
    public static final String KEY_BUYER_MOBILE_NO = "buyer_mobileno";
    public static final String KEY_BUYER_COUNTRY = "buyer_country";
    public static final String KEY_BUYER_CITY = "buyer_city";
    public static final String KEY_BUYER_GENDER = "buyer_gender";
    public static final String KEY_BUYER_DOB = "buyer_dob";
    public static final String KEY_BUYER_ADDRESS = "buyer_address";
    public static final String KEY_BUYER_PROFILE = "buyer_profile";


    public static final String KEY_SELLER_BUSINESS_NAME = "business_name";
    public static final String KEY_SELLER_BUSINESS_COUNTY = "business_country";
    public static final String KEY_SELLER_BUSINESS_CITY = "business_city";


    //
    public static final String KEY_SELLER_TEMP_BUSINESS_COUNTY = "temp_business_country";
    public static final String KEY_SELLER_TEMP_BUSINESS_CITY = "temp_business_city";


    //---------------------------------------WebServices---------------------------------------------------------------------------------------*/

//    public static final String BASE_URL = "http://tradegenie.m-staging.in/admin/android/";
//    public static final String BASE_URL = "http://tradegenie.m-staging.in/android/";
//    public static final String BASE_URL = "http://www.tradegenie.appylause.com/android/";
    public static final String BASE_URL = "http://www.appylause.com/android/";
    public static final String URL_COUNTRY_LIST = BASE_URL + "country-list";
    public static final String URL_COUNTRY_LIST_HOME = BASE_URL + "country-home-list";
    public static final String URL_USER_REGISTRATION = BASE_URL + "user-registration";
    public static final String URL_RE_SEND_OTP = BASE_URL + "re-send-otp";
    public static final String URL_OTP_VERIFICATION = BASE_URL + "otp-verification";
    public static final String URL_PERSONAL_DETAILS = BASE_URL + "personal-details";
    public static final String URL_PERSONAL_ACTION = BASE_URL + "personal-action";
    public static final String URL_BUSINESS_DETAILS = BASE_URL + "business-details";
    public static final String URL_BUSINESS_DETAILS_ACTION = BASE_URL + "business-details-action";
    public static final String URL_VIDEO_LINK_ADD = BASE_URL + "video-link-add";
    public static final String URL_DOCUMENT = BASE_URL + "document";
    public static final String URL_CERTIFICATE = BASE_URL + "certificate";
    public static final String URL_OTHER_DETAILS = BASE_URL + "other-details";
    public static final String URL_DOCUMENT_DELETE = BASE_URL + "document-delete";
    public static final String URL_CERTIFICATES_DELETE = BASE_URL + "certificates-delete";
    public static final String URL_CATEGORY_LIST = BASE_URL + "category-list";
    public static final String URL_GET_SUBCATEGORY_ATTRIBUTE = BASE_URL + "get-subcategory-attribute";
    public static final String URL_SUB_SUB_CATEGORY = BASE_URL + "sub-sub-category";
    public static final String URL_SELLER_LIST = BASE_URL + "seller-list";
    public static final String URL_MODEOFPAY_LIST = BASE_URL + "modeofpay-list";
    public static final String URL_MODEOFSUPPLY_LIST = BASE_URL + "modeofsupply-list";
    public static final String URL_GET_ATTRIBUTE_TYPE = BASE_URL + "get-attribute-type";
    public static final String URL_DASHBOARD = BASE_URL + "dashboard";
    public static final String URL_PRODUCT_ADD = BASE_URL + "product-add";
    public static final String URL_PRODUCT_EDIT = BASE_URL + "product-edit";
    public static final String URL_PRODUCT_LIST = BASE_URL + "product-list";
    public static final String URL_PRODUCT_DELETE = BASE_URL + "product-delete";
    public static final String URL_BROCHURE_UPDATE = BASE_URL + "brochure-update";
    public static final String URL_FIRM_LIST = BASE_URL + "firm-list";
    public static final String URL_NATURE_BUSINESS_LIST = BASE_URL + "nature-business-list";
    public static final String URL_CITY_LIST = BASE_URL + "city-list";
    public static final String URL_GET_CURRENCY = BASE_URL + "get-currency";
    public static final String URL_IMAGE_COUNT = BASE_URL + "image-count";
    public static final String URL_GET_PRODUCT_DETAILS = BASE_URL + "geteditproduct";
    public static final String URL_GET_SALES_TYPE_LIST = BASE_URL + "sales-type-list";
    public static final String URL_CMS = BASE_URL + "cms";
    public static final String URL_OTP_EMPLTY = BASE_URL + "otp-empty";
    public static final String URL_PRODUCT_VIEW = BASE_URL + "product-view";
    public static final String URL_BROCHURE_IMAGE_UPDATE = BASE_URL + "brochure-image-update";
    public static final String URL_SEARCH = BASE_URL + "search";
    public static final String URL_ENQUIRY_SUBMIT = BASE_URL + "enquiry-submit";
    public static final String URL_ENQUIRY_DETAILS = BASE_URL + "enquiry-details";
    public static final String URL_FEVORITE_SUBMIT = BASE_URL + "fevorite-submit";
    public static final String URL_NOTIFICATION_LIST = BASE_URL + "notificationlist";
    public static final String URL_NOTIFICATION_CLEAR = BASE_URL + "notification-clear";
    public static final String URL_BANNER_LIST = BASE_URL + "bannerlist";
    public static final String URL_CATEGORY_ADV = BASE_URL + "category-adv";
    public static final String URL_SUB_CATEGORY_ADV = BASE_URL + "subcategory-adv";
    public static final String URL_SUB_SUB_CATEGORY_ADV = BASE_URL + "subpsubcategory-adv";
    public static final String URL_SUBSCRIPTION_LIST = BASE_URL + "subscription-list";
    public static final String URL_MY_SUBSCRIPTION_LIST = BASE_URL + "my-subscription-list";
    public static final String URL_FAVOURITES_LIST = BASE_URL + "favourite-list";
    public static final String URL_SUBS_BUYNOW = BASE_URL + "subs-buynow";
    public static final String URL_NEW_LEADS_LIST = BASE_URL + "newleads-list";
    public static final String URL_NEW_LEADS_DETAILS = BASE_URL + "newleads-details";
    public static final String URL_ENQUIRY_VIEWS = BASE_URL + "enquiry-views";
    public static final String URL_PAY_NOW = BASE_URL + "paynow";
    public static final String URL_ENQUIRY_LIST = BASE_URL + "enquiry-list";
    public static final String URL_MY_LEADS = BASE_URL + "my-leads";
    public static final String URL_POST_REQ_PRODUCT_LIST = BASE_URL + "post-req-product-list";
    public static final String URL_SALES_TYPE = BASE_URL + "prefered-type";
    public static final String URL_PREFEREED_TYPE = BASE_URL + "prefered-type";
    public static final String URL_ENQUIRY_ASSIGN_ATTRIBUTE = BASE_URL + "enquiry-assign-attribute";
    public static final String URL_POSTENQUIRY_SUBMIT = BASE_URL + "postenquiry-submit";
    public static final String URL_GET_LEADS = BASE_URL + "get-leads";
    //public static final String URL_POST_ENQUIRY_LIST = BASE_URL + "post-enquiry-list";
    public static final String URL_POST_ENQUIRY_LIST = BASE_URL + "post-enquiry-list-new";
    public static final String URL_TYPE_OF_BUYER = BASE_URL + "type-of-buyer";
    public static final String URL_GET_PAYMENT_HISTORY = BASE_URL + "get-payment-history";
    public static final String URL_GET_CHAT_DETAILS = BASE_URL + "chat-details";
    public static final String URL_CHAT_INSERT = BASE_URL + "chat-insert";
    public static final String URL_MIN_MAX_PRICE = BASE_URL + "min-max-price";
    public static final String URL_FILTER = BASE_URL + "filter";
    public static final String URL_CHATING_LIST = BASE_URL + "chating-list";
    public static final String URL_SUBSCRIPTION_DETALS = BASE_URL + "subscription-details";
    public static final String URL_SUBSCRIPTION_UPDATE = BASE_URL + "subscription-update";
    public static final String URL_BECOME_SELLER = BASE_URL + "become-seller";
    public static final String URL_GET_BUSINESS_DETAILS = BASE_URL + "get-business-details";
    public static final String URL_GET_CHECK_SELLER = BASE_URL + "check-seller";
    public static final String URL_RATING_GET = BASE_URL + "rating-get";
    public static final String URL_RATING_SUBMIT = BASE_URL + "rating-submit";
    public static final String URL_DOWNLOAD_INVOICE = BASE_URL + "payment-download-invoice";
    public static final String URL_GET_PRODUCT_TAG = BASE_URL + "get-product-tags";
    public static final String URL_SUBMIT_TENDER = BASE_URL + "tender-submit";
    public static final String URL_CLEAR_TENDER = BASE_URL + "tender-file-delete";
    public static final String URL_ADD_DOC = BASE_URL + "tender-file-upload";
    public static final String URL_MY_POST_TENDER_LIST = BASE_URL + "my-post-tender-list";
    public static final String URL_TENDER_ALL_FILES_DELETE = BASE_URL + "tender-file-all-delete";


    public static final String URL_MY_TENDER_LIST = BASE_URL + "tender-purchase-list";
    public static final String URL_NEW_TENDER_LIST = BASE_URL + "new-tender-list";
    public static final String URL_NEW_TENDER_DETAIL = BASE_URL + "new-tender-details";
    public static final String URL_PAY_FOR_TENDER = BASE_URL + "pay-new-tender";
    public static final String URL_MY_TENDER_DETAIL = BASE_URL + "tender-purchase-view";
    public static final String URL_MY_TENDER_DETAIL_MY_POST = BASE_URL + "tender-my-view";

    public static final String URL_GET_USER_LANGUAGE = BASE_URL + "get-user-language";
    public static final String URL_UPDATE_USER_LANGUAGE = BASE_URL + "update-user-language";



    public static final String URL_CHECK_REMAINING_SUBSCRIPTION = BASE_URL + "check-remaining-subscription";
    public static final String URL_CHECK_REMAINING_SUBSCRIPTION_TENDER = BASE_URL + "remaining-subscription-tender";

    public static final String URL_GET_HOME_REQUIREMENT_LIST = BASE_URL + "home-post-req-list";

}
