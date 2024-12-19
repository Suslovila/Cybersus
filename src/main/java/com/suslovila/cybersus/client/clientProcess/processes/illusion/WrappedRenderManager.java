package com.suslovila.cybersus.client.clientProcess.processes.illusion;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.util.ReportedException;

public class WrappedRenderManager {
    public static boolean doRenderEntity(Render render, Entity p_147939_1_, double p_147939_2_, double p_147939_4_, double p_147939_6_, float p_147939_8_, float p_147939_9_, boolean p_147939_10_)
    {
        RenderManager manager = RenderManager.instance;
        try
        {

            if (render != null && manager.renderEngine != null)
            {
                if (!render.isStaticEntity() || p_147939_10_)
                {
                    try
                    {
                        render.doRender(p_147939_1_, p_147939_2_, p_147939_4_, p_147939_6_, p_147939_8_, p_147939_9_);
                    }
                    catch (Throwable throwable2)
                    {
                        throw new ReportedException(CrashReport.makeCrashReport(throwable2, "Rendering entity in world"));
                    }

                    try
                    {
                        render.doRenderShadowAndFire(p_147939_1_, p_147939_2_, p_147939_4_, p_147939_6_, p_147939_8_, p_147939_9_);
                    }
                    catch (Throwable throwable1)
                    {
                        throw new ReportedException(CrashReport.makeCrashReport(throwable1, "Post-rendering entity in world"));
                    }

                    if (manager.debugBoundingBox && !p_147939_1_.isInvisible() && !p_147939_10_)
                    {
                        try
                        {
                            manager.renderDebugBoundingBox(p_147939_1_, p_147939_2_, p_147939_4_, p_147939_6_, p_147939_8_, p_147939_9_);
                        }
                        catch (Throwable throwable)
                        {
                            throw new ReportedException(CrashReport.makeCrashReport(throwable, "Rendering entity hitbox in world"));
                        }
                    }
                }
            }
            else if (manager.renderEngine != null)
            {
                return false;
            }

            return true;
        }
        catch (Throwable throwable3)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable3, "Rendering entity in world");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being rendered");
            p_147939_1_.addEntityCrashInfo(crashreportcategory);
            CrashReportCategory crashreportcategory1 = crashreport.makeCategory("Renderer details");
            crashreportcategory1.addCrashSection("Assigned renderer", render);
            crashreportcategory1.addCrashSection("Location", CrashReportCategory.func_85074_a(p_147939_2_, p_147939_4_, p_147939_6_));
            crashreportcategory1.addCrashSection("Rotation", Float.valueOf(p_147939_8_));
            crashreportcategory1.addCrashSection("Delta", Float.valueOf(p_147939_9_));
            throw new ReportedException(crashreport);
        }
    }


}
