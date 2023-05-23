package tomk.render;

import net.ccbluex.liquidbounce.utils.render.RenderUtils;

public class AnimationUtils {
    public static float animate(float target, float current, float speed) {
        if (current == target) return current;

        boolean larger = target > current;
        if (speed < 0.0F) {
            speed = 0.0F;
        } else if (speed > 1.0F) {
            speed = 1.0F;
        }

        double dif = Math.max(target, (double)current) - Math.min(target, (double)current);
        double factor = dif * (double)speed;
        if (factor < 0.1D) {
            factor = 0.1D;
        }

        if (larger) {
            current += (float)factor;
            if (current >= target) current = target;
        } else if (target < current) {
            current -= (float)factor;
            if (current <= target) current = target;
        }

        return current;
    }
    public static float lstransition(float now, float desired, double speed) {
        double dif = (double) Math.abs(desired - now);
        float a = (float) Math.abs((double) (desired - (desired - Math.abs(desired - now))) / (100.0D - speed * 10.0D));
        float x = now;

        if (dif > 0.0D) {
            if (now < desired) {
                x = now + a * (float) RenderUtils.deltaTime;
            } else if (now > desired) {
                x = now - a * (float) RenderUtils.deltaTime;
            }
        } else {
            x = desired;
        }

        if ((double) Math.abs(desired - x) < 0.01D && x != desired) {
            x = desired;
        }

        return x;
    }

    public static float easeOut(float t, float d) {
        return (t = t / d - 1.0F) * t * t + 1.0F;
    }

    public static float easeOutElastic(float x) {
        double c4 = 2.0943951023931953D;

        return x == 0.0F ? 0.0F : (float) (x == 1.0F ? 1.0D : Math.pow(2.0D, (double) (-10.0F * x)) * Math.sin(((double) (x * 10.0F) - 0.75D) * c4) + 1.0D);
    }
}
