package com.raphydaphy.arcanemagic.client.model;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.render.model.json.ModelElementFace;
import net.minecraft.client.render.model.json.ModelElementTexture;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class CustomItemModelGenerator {
    public static final List<String> LAYERS = Lists.newArrayList("layer0", "layer1", "layer2", "layer3", "layer4");

    public JsonUnbakedModel create(Function<Identifier, Sprite> spriteGetter, JsonUnbakedModel unbakedModel) {
        Map<String, String> layers = Maps.newHashMap();
        List<ModelElement> elements = Lists.newArrayList();

        for (int int_1 = 0; int_1 < LAYERS.size(); ++int_1) {
            String string_1 = LAYERS.get(int_1);
            if (!unbakedModel.textureExists(string_1)) {
                break;
            }

            String string_2 = unbakedModel.resolveTexture(string_1);
            layers.put(string_1, string_2);
            Sprite base = spriteGetter.apply(new Identifier(ArcaneMagic.DOMAIN, "item/iron_dagger"));
            Sprite sprite_1 = spriteGetter.apply(new Identifier(string_2));
            elements.addAll(this.createModelElements(int_1, string_1, sprite_1, base));
        }

        layers.put("particle", unbakedModel.textureExists("particle") ? unbakedModel.resolveTexture("particle") : (String) layers.get("layer0"));
        JsonUnbakedModel jsonUnbakedModel_2 = new JsonUnbakedModel(null, elements, layers, false, false, unbakedModel.getTransformations(), unbakedModel.getOverrides());
        jsonUnbakedModel_2.id = unbakedModel.id;
        return jsonUnbakedModel_2;
    }

    private List<ModelElement> createModelElements(int layerNumber, String layerName, Sprite sprite, Sprite base) {
        Map<Direction, ModelElementFace> textures = Maps.newHashMap();
        textures.put(Direction.SOUTH, new ModelElementFace(null, layerNumber, layerName, new ModelElementTexture(new float[]{0, 0, 16, 16}, 0)));
        textures.put(Direction.NORTH, new ModelElementFace(null, layerNumber, layerName, new ModelElementTexture(new float[]{16, 0, 0, 16}, 0)));
        List<ModelElement> elements = Lists.newArrayList();
        // Front face
        elements.add(new ModelElement(new Vector3f(0, 0, 7.5f), new Vector3f(16, 16, 8.5f), textures, null, true));
        elements.addAll(this.method_3481(sprite, base, layerName, layerNumber));
        return elements;
    }

    private List<ModelElement> method_3481(Sprite layerSprite, Sprite baseSprite, String layerName, int layerID) {
        boolean base = layerName.equals("layer0");

        float float_1 = (float) layerSprite.getWidth();
        float float_2 = (float) layerSprite.getHeight();
        List<ModelElement> list_1 = Lists.newArrayList();

        for (Face face : this.getFaces(layerSprite)) {
            FaceDirection faceDirection = face.getDirection();

            float back = 7.5f;
            float front = 8.5f;
            float top = 16.0f;

            float float_3 = 0;
            float startY = 0;
            float float_5 = 0;
            float endY = 0;
            float float_7 = 0;
            float float_8 = 0;
            float float_9 = 0;
            float float_10 = 0;
            float float_11 = 16 / float_1;
            float float_12 = 16 / float_2;
            float float_13 = (float) face.getWidth();
            float float_14 = (float) face.method_3485();
            float float_15 = (float) face.getHeight();

            switch (faceDirection) {
                case UP:
                    float_7 = float_13;
                    float_3 = float_13;
                    float_5 = float_8 = float_14 + 1;
                    float_9 = float_15;
                    startY = float_15;
                    endY = float_15;
                    float_10 = float_15 + 1;
                    break;
                case DOWN:
                    float_9 = float_15;
                    float_10 = float_15 + 1;
                    float_7 = float_13;
                    float_3 = float_13;
                    float_5 = float_8 = float_14 + 1;
                    startY = float_15 + 1;
                    endY = float_15 + 1;
                    break;
                case LEFT:
                    float_7 = float_15;
                    float_3 = float_15;
                    float_5 = float_15;
                    float_8 = float_15 + 1;
                    float_10 = float_13;
                    startY = float_13;
                    endY = float_9 = float_14 + 1;
                    break;
                case RIGHT:
                    float_7 = float_15;
                    float_8 = float_15 + 1;
                    float_3 = float_15 + 1;
                    float_5 = float_15 + 1;
                    float_10 = float_13;
                    startY = float_13;
                    endY = float_9 = float_14 + 1;
            }

            float_3 *= float_11;
            float_5 *= float_11;
            startY *= float_12;
            endY *= float_12;
            startY = 16 - startY;
            endY = 16 - endY;
            float_7 *= float_11;
            float_8 *= float_11;
            float_9 *= float_12;
            float_10 *= float_12;
            Map<Direction, ModelElementFace> map_1 = Maps.newHashMap();
            map_1.put(faceDirection.method_3488(), new ModelElementFace(null, layerID, layerName, new ModelElementTexture(new float[]{float_7, float_9, float_8, float_10}, 0)));
            switch (faceDirection) {
                case UP:
                    list_1.add(new ModelElement(new Vector3f(float_3, startY, back), new Vector3f(float_5, startY, front), map_1, null, true));
                    break;
                case DOWN:
                    list_1.add(new ModelElement(new Vector3f(float_3, endY, back), new Vector3f(float_5, endY, front), map_1, null, true));
                    break;
                case LEFT:
                    list_1.add(new ModelElement(new Vector3f(float_3, startY, back), new Vector3f(float_3, endY, front), map_1, null, true));
                    break;
                case RIGHT:
                    list_1.add(new ModelElement(new Vector3f(float_5, startY, back), new Vector3f(float_5, endY, front), map_1, null, true));
            }
        }

        return list_1;
    }

    private List<Face> getFaces(Sprite sprite_1) {
        int width = sprite_1.getWidth();
        int height = sprite_1.getHeight();
        List<Face> list_1 = Lists.newArrayList();

        for (int int_3 = 0; int_3 < sprite_1.getFrameCount(); ++int_3) {
            for (int int_4 = 0; int_4 < height; ++int_4) {
                for (int int_5 = 0; int_5 < width; ++int_5) {
                    boolean boolean_1 = !this.method_3477(sprite_1, int_3, int_5, int_4, width, height);
                    this.method_3476(FaceDirection.UP, list_1, sprite_1, int_3, int_5, int_4, width, height, boolean_1);
                    this.method_3476(FaceDirection.DOWN, list_1, sprite_1, int_3, int_5, int_4, width, height, boolean_1);
                    this.method_3476(FaceDirection.LEFT, list_1, sprite_1, int_3, int_5, int_4, width, height, boolean_1);
                    this.method_3476(FaceDirection.RIGHT, list_1, sprite_1, int_3, int_5, int_4, width, height, boolean_1);
                }
            }
        }

        return list_1;
    }

    private void method_3476(FaceDirection itemModelGenerator$faceDirection_1, List<Face> faces, Sprite sprite_1, int int_1, int int_2, int int_3, int int_4, int int_5, boolean boolean_1) {
        boolean boolean_2 = this.method_3477(sprite_1, int_1, int_2 + itemModelGenerator$faceDirection_1.method_3490(), int_3 + itemModelGenerator$faceDirection_1.method_3489(), int_4, int_5) && boolean_1;
        if (boolean_2) {
            this.method_3482(faces, itemModelGenerator$faceDirection_1, int_2, int_3);
        }

    }

    private void method_3482(List<Face> list_1, FaceDirection aSpecificClass802, int int_1, int int_2) {
        Face itemModelGenerator$face_1 = null;

        for (Face face_instance : list_1) {
            if (face_instance.getDirection() == aSpecificClass802) {
                int int_3 = aSpecificClass802.method_3491() ? int_2 : int_1;
                if (face_instance.getHeight() == int_3) {
                    itemModelGenerator$face_1 = face_instance;
                    break;
                }
            }
        }

        int int_4 = aSpecificClass802.method_3491() ? int_2 : int_1;
        int int_5 = aSpecificClass802.method_3491() ? int_1 : int_2;
        if (itemModelGenerator$face_1 == null) {
            list_1.add(new Face(aSpecificClass802, int_5, int_4));
        } else {
            itemModelGenerator$face_1.method_3483(int_5);
        }

    }

    private boolean method_3477(Sprite sprite_1, int int_1, int int_2, int int_3, int int_4, int int_5) {
        return int_2 < 0 || int_3 < 0 || int_2 >= int_4 || int_3 >= int_5 || sprite_1.method_4583(int_1, int_2, int_3);
    }

    @Environment(EnvType.CLIENT)
    enum FaceDirection {
        UP(Direction.UP, 0, -1),
        DOWN(Direction.DOWN, 0, 1),
        LEFT(Direction.EAST, -1, 0),
        RIGHT(Direction.WEST, 1, 0);

        private final Direction field_4276;
        private final int field_4280;
        private final int field_4279;

        FaceDirection(Direction direction_1, int int_1, int int_2) {
            this.field_4276 = direction_1;
            this.field_4280 = int_1;
            this.field_4279 = int_2;
        }

        public Direction method_3488() {
            return this.field_4276;
        }

        public int method_3490() {
            return this.field_4280;
        }

        public int method_3489() {
            return this.field_4279;
        }

        private boolean method_3491() {
            return this == DOWN || this == UP;
        }
    }

    @Environment(EnvType.CLIENT)
    static class Face {
        private final FaceDirection direction;
        private final int height;
        private int width;
        private int field_4273;

        Face(FaceDirection direction, int width, int height) {
            this.direction = direction;
            this.width = width;
            this.field_4273 = width;
            this.height = height;
        }

        void method_3483(int int_1) {
            if (int_1 < this.width) {
                this.width = int_1;
            } else if (int_1 > this.field_4273) {
                this.field_4273 = int_1;
            }

        }

        FaceDirection getDirection() {
            return this.direction;
        }

        int getWidth() {
            return this.width;
        }

        int method_3485() {
            return this.field_4273;
        }

        int getHeight() {
            return this.height;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof Face)) {
                return false;
            }
            Face ofd = (Face) other;
            return this.direction == ofd.direction && this.height == ofd.width && this.field_4273 == ofd.field_4273 && this.height == ofd.height;
        }
    }
}
