package xyz.fadli.pasbeli.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A generic class that holds a value with its loading fetchStatus.
 * @param <T>
 */
public class Resource<T> {

    @NonNull
    public final FetchStatus fetchStatus;

    @Nullable
    private final String message;

    @Nullable
    public final T data;

    public Resource(@NonNull FetchStatus fetchStatus, @Nullable T data, @Nullable String message) {
        this.fetchStatus = fetchStatus;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@Nullable T data) {
        return new Resource<>(FetchStatus.SUCCESS, data, null);
    }


    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(FetchStatus.ERROR, data, msg);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(FetchStatus.LOADING, data, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Resource<?> resource = (Resource<?>) o;

        if (fetchStatus != resource.fetchStatus) {
            return false;
        }
        if (!Objects.equals(message, resource.message)) {
            return false;
        }
        return Objects.equals(data, resource.data);
    }

    @Override
    public int hashCode() {
        int result = fetchStatus.hashCode();
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @NotNull
    @Override
    public String toString() {
        return "Resource{" +
                "fetchStatus=" + fetchStatus +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
