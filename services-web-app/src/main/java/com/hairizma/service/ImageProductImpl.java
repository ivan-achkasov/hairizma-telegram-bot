package com.hairizma.service;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.net.URL;

@Service
public class ImageProductImpl implements ImageService {
    @Override
    public Response getImage(long id) {
        try {
            final URL url = new URL("https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Telegram_2019_Logo.svg/1200px-Telegram_2019_Logo.svg.png");
            return Response.ok(url.openStream()).build();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
