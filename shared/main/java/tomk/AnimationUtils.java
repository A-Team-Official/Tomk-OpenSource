package tomk;

import net.ccbluex.liquidbounce.utils.render.RenderUtils;

public final class AnimationUtils {
    public static double animate(double target, double current, double speed) {
        boolean larger = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        } else if (speed > 1.0) {
            speed = 1.0;
        }

        double dif = Math.max(target, current) - Math.min(target, current);
        if (dif < 0.1){
            return target;
        }
        double factor = dif * speed;
        if (factor < 0.1) {
            factor = 0.1;
        }
        if (larger){
            if (current + factor>target){
                current = target;
            }else {
                current += factor;
            }
        }else {
            if (current - factor<target) {
                current = target;
            }else {
                current -= factor;
            }
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
}

