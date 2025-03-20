package com.example.utils;


import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import static org.monte.media.AudioFormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

public class CustomScreenRecorder extends ScreenRecorder {

    private String videoFileName; // Store the final filename

    public CustomScreenRecorder(GraphicsConfiguration cfg, Rectangle captureArea, File movieFolder, String fileName) throws IOException, AWTException {
        super(cfg, captureArea,
                new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                        CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                        DepthKey, 24, FrameRateKey, Rational.valueOf(15), QualityKey, 1.0f,
                        KeyFrameIntervalKey, 15),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black",
                        FrameRateKey, Rational.valueOf(30)),
                null, movieFolder);

        this.videoFileName = fileName + ".avi"; // Append extension
    }

    @Override
    protected File createMovieFile(Format fileFormat) {
        return new File(this.movieFolder, videoFileName);
    }
}
