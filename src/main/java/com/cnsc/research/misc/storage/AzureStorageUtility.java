package com.cnsc.research.misc.storage;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.azure.storage.common.StorageSharedKeyCredential;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class AzureStorageUtility extends StorageUtility {
    public static final String PDF_CONTAINER = "pdf";
    public static final String DOCUMENT_CONTAINER = "documents";
    private final BlobServiceClient blobServiceClient;
    private BlobContainerClient blobContainerClient;


    public AzureStorageUtility() {
        StorageSharedKeyCredential credential = new StorageSharedKeyCredential("arkibo",
                "r6GA0bC+mzxJtzk5iIjOpibQADUCOLHydIP1692aQ4NiH1AiTOQYD57VXczbOrjNLjjmWHj23ModKeG03syINQ==");
        blobServiceClient = new BlobServiceClientBuilder()
                .endpoint("https://arkibo.blob.core.windows.net")
                .sasToken("r6GA0bC+mzxJtzk5iIjOpibQADUCOLHydIP1692aQ4NiH1AiTOQYD57VXczbOrjNLjjmWHj23ModKeG03syINQ==")
                .credential(credential)
                .buildClient();

    }

    public void upload(byte[] streamData, String filename) throws IOException {
        BlockBlobClient blobClient = blobContainerClient.getBlobClient(filename).getBlockBlobClient();
        InputStream dataStream = new ByteArrayInputStream(streamData);
        blobClient.upload(dataStream, streamData.length);
        dataStream.close();
    }

    public void uploadOverwrite(byte[] streamData, String filename) throws IOException {
        BlockBlobClient blobClient = blobContainerClient.getBlobClient(filename).getBlockBlobClient();
        InputStream dataStream = new ByteArrayInputStream(streamData);
        blobClient.upload(dataStream, streamData.length, true);
        dataStream.close();
    }

    public void delete(String filename) {
        BlockBlobClient blobClient = blobContainerClient.getBlobClient(filename).getBlockBlobClient();
        blobClient.delete();
    }

    public AzureStorageUtility inContainer(String container) {
        blobContainerClient = blobServiceClient.getBlobContainerClient(container);
        return this;
    }

}
