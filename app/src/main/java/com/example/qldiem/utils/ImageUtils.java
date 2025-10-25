package com.example.qldiem.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Utility class cho xử lý hình ảnh - cung cấp các phương thức chuyển đổi và xử lý hình ảnh
 *
 * CÔNG DỤNG:
 * - Cung cấp các phương thức tiện ích để xử lý hình ảnh trong ứng dụng
 * - Hỗ trợ chuyển đổi giữa các định dạng hình ảnh (Bitmap, Base64, Uri)
 * - Hỗ trợ resize hình ảnh để tiết kiệm bộ nhớ và băng thông
 * - Dùng để lưu trữ và hiển thị hình ảnh trong cơ sở dữ liệu và giao diện người dùng
 *
 * CÁC PHƯƠNG THỨC CHÍNH:
 * - bitmapToBase64: Chuyển đổi Bitmap thành chuỗi Base64 để lưu trong cơ sở dữ liệu
 * - base64ToBitmap: Chuyển đổi chuỗi Base64 thành Bitmap để hiển thị trong giao diện
 * - resizeBitmap: Thay đổi kích thước Bitmap để tiết kiệm bộ nhớ
 * - getBitmapFromUri: Đọc Bitmap từ Uri để xử lý hình ảnh từ thư viện
 * - uriToBase64: Chuyển đổi trực tiếp Uri thành Base64 (kết hợp các phương thức trên)
 *
 * NẾU KHÔNG CÓ CLASS NÀY:
 * - Không thể xử lý và lưu trữ hình ảnh trong ứng dụng
 * - Không thể hiển thị hình ảnh sinh viên, avatar, v.v.
 * - Ứng dụng sẽ tiêu tốn nhiều bộ nhớ và băng thông
 * - Tính năng hình ảnh sẽ không hoạt động
 */
public class ImageUtils {

    /**
     * Hàm chuyển đổi Bitmap thành chuỗi Base64
     *
     * CÔNG DỤNG:
     * - Chuyển đổi hình ảnh Bitmap thành chuỗi Base64 để lưu trong cơ sở dữ liệu
     * - Dùng khi cần lưu trữ hình ảnh dưới dạng chuỗi văn bản
     * - Hỗ trợ nén hình ảnh theo định dạng PNG với chất lượng 100%
     *
     * CÁC BIẾN TRONG HÀM:
     * - byteArrayOutputStream: ByteArrayOutputStream để chứa dữ liệu nhị phân của hình ảnh
     * - byteArray: mảng byte chứa dữ liệu nhị phân của hình ảnh đã nén
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Bitmap bitmap: đối tượng Bitmap cần chuyển đổi
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: chuỗi Base64 đại diện cho hình ảnh
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể chuyển đổi Bitmap thành chuỗi để lưu trong cơ sở dữ liệu
     * - Không thể lưu trữ hình ảnh dưới dạng văn bản
     * - Ứng dụng sẽ không thể lưu avatar sinh viên
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        if (bitmap == null) return null;
        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    /**
     * Hàm chuyển đổi chuỗi Base64 thành Bitmap
     *
     * CÔNG DỤNG:
     * - Chuyển đổi chuỗi Base64 thành Bitmap để hiển thị trong giao diện
     * - Dùng khi cần hiển thị hình ảnh đã lưu trong cơ sở dữ liệu
     * - Hỗ trợ giải mã chuỗi Base64 thành dữ liệu nhị phân hình ảnh
     *
     * CÁC BIẾN TRONG HÀM:
     * - decodedBytes: mảng byte chứa dữ liệu nhị phân đã giải mã từ chuỗi Base64
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String base64Str: chuỗi Base64 đại diện cho hình ảnh
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - Bitmap: đối tượng Bitmap được tạo từ chuỗi Base64
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể chuyển đổi chuỗi Base64 thành Bitmap để hiển thị
     * - Không thể hiển thị hình ảnh đã lưu trong cơ sở dữ liệu
     * - Giao diện sẽ không thể hiển thị avatar sinh viên
     */
    public static Bitmap base64ToBitmap(String base64Str) {
        if (base64Str == null || base64Str.isEmpty()) return null;
        
        try {
            byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Hàm thay đổi kích thước Bitmap để tiết kiệm bộ nhớ
     *
     * CÔNG DỤNG:
     * - Thay đổi kích thước Bitmap để giảm tiêu thụ bộ nhớ
     * - Duy trì tỷ lệ khung hình khi thay đổi kích thước
     * - Dùng khi cần hiển thị hình ảnh với kích thước nhỏ hơn
     *
     * CÁC BIẾN TRONG HÀM:
     * - width, height: chiều rộng và chiều cao gốc của hình ảnh
     * - ratioBitmap: tỷ lệ khung hình của hình ảnh gốc
     * - ratioMax: tỷ lệ khung hình tối đa cho phép
     * - finalWidth, finalHeight: chiều rộng và chiều cao mới sau khi thay đổi kích thước
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Bitmap bitmap: đối tượng Bitmap cần thay đổi kích thước
     * - int maxWidth: chiều rộng tối đa cho phép
     * - int maxHeight: chiều cao tối đa cho phép
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - Bitmap: đối tượng Bitmap mới với kích thước đã được thay đổi
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể giảm kích thước hình ảnh để tiết kiệm bộ nhớ
     * - Ứng dụng sẽ tiêu tốn nhiều bộ nhớ khi xử lý hình ảnh lớn
     * - Có thể gây lỗi OutOfMemoryError khi xử lý nhiều hình ảnh
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        if (bitmap == null) return null;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float ratioBitmap = (float) width / (float) height;
        float ratioMax = (float) maxWidth / (float) maxHeight;

        int finalWidth = maxWidth;
        int finalHeight = maxHeight;
        
        if (ratioMax > ratioBitmap) {
            finalWidth = (int) ((float) maxHeight * ratioBitmap);
        } else {
            finalHeight = (int) ((float) maxWidth / ratioBitmap);
        }

        return Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, true);
    }

    /**
     * Hàm đọc Bitmap từ Uri
     *
     * CÔNG DỤNG:
     * - Đọc Bitmap từ Uri để xử lý hình ảnh từ thư viện thiết bị
     * - Dùng khi người dùng chọn hình ảnh từ Gallery, Photos, v.v.
     * - Mở InputStream để đọc dữ liệu nhị phân của hình ảnh từ Uri
     *
     * CÁC BIẾN TRONG HÀM:
     * - inputStream: InputStream để đọc dữ liệu từ Uri
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Context context: context của ứng dụng để truy cập ContentResolver
     * - Uri uri: Uri của hình ảnh cần đọc
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - Bitmap: đối tượng Bitmap được đọc từ Uri
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể đọc hình ảnh từ thư viện thiết bị
     * - Người dùng không thể chọn ảnh từ Gallery
     * - Tính năng chọn ảnh sẽ không hoạt động
     */
    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Hàm chuyển đổi Uri thành chuỗi Base64 (với resize)
     *
     * CÔNG DỤNG:
     * - Chuyển đổi trực tiếp Uri thành chuỗi Base64 để lưu trong cơ sở dữ liệu
     * - Tự động resize hình ảnh để tiết kiệm không gian lưu trữ
     * - Kết hợp các phương thức getBitmapFromUri, resizeBitmap và bitmapToBase64
     *
     * CÁC BIẾN TRONG HÀM:
     * - bitmap: Bitmap đọc được từ Uri
     * - resized: Bitmap đã được resize để tiết kiệm không gian
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Context context: context của ứng dụng để truy cập ContentResolver
     * - Uri uri: Uri của hình ảnh cần chuyển đổi
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: chuỗi Base64 đại diện cho hình ảnh (đã được resize)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể chuyển đổi trực tiếp Uri thành Base64
     * - Người dùng chọn ảnh nhưng không thể lưu trữ được
     * - Tính năng chọn và lưu ảnh sẽ không hoạt động hoàn chỉnh
     */
    public static String uriToBase64(Context context, Uri uri) {
        Bitmap bitmap = getBitmapFromUri(context, uri);
        if (bitmap == null) return null;
        
        // Resize để tiết kiệm không gian
        Bitmap resized = resizeBitmap(bitmap, 500, 500);
        return bitmapToBase64(resized);
    }
}
